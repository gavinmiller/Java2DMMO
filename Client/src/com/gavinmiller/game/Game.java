package com.gavinmiller.game;

import com.gavinmiller.shared.PlayerPacket;
import java.awt.Canvas;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import com.gavinmiller.client.MainClient;

/**
 * Main game class containing game loop, render loop, and all objects in the scene
 * @author Gavin Miller
 * @version 0.1
 */
public class Game extends JFrame implements Runnable {

    public static int alpha = 0xFFFF00DC; // Transparent colour not to display
    private final int MAX_GAMEOBJECTS = 64;

    private Canvas canvas = new Canvas();
    private KeyboardListener keyListener = new KeyboardListener(this);
    private MouseEventListener mouseListener = new MouseEventListener(this);
    private RenderHandler renderer;

    private SpriteSheet sheet;
    private SpriteSheet playerSheet;
    private Tiles tiles;
    private Map map;
    
    private GameObject[] gameObjects;
    private Player player;
    
    private int currentXZoom, currentYZoom;
    
    private int selectedTileID = 2;
    
    private ArrayList<NetworkPlayer> networkPlayers;
    
    // Networking stuff
    private MainClient mainClient;

    public Game(MainClient mainClient) {
        
        
        
        //Make our program shutdown when we exit out.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set the position and size of our frame.
        setBounds(0, 0, 1920, 1080);

        setResizable(false);

        //Put our frame in the center of the screen.
        setLocationRelativeTo(null);

        //Add our graphics compoent
        add(canvas);

        //Make our frame visible.
        setVisible(true);

        //Create our object for buffer strategy.
        canvas.createBufferStrategy(3);
        
        // Set zoom amount
        currentXZoom = 5;
        currentYZoom = 5;
        
        // Create renderer
        renderer = new RenderHandler(getWidth(), getHeight());
        
        // Load assets
        BufferedImage sheetImage = loadImage("Tiles1.png");
        sheet = new SpriteSheet(sheetImage);
        sheet.loadSprites(16, 16);
        
        BufferedImage playerSheetImage = loadImage("Player.png");
        playerSheet = new SpriteSheet(playerSheetImage);
        playerSheet.loadSprites(20, 26);
        
        // load tiles
        tiles = new Tiles(new File("Tiles.txt"), sheet);

        // load map
        map = new Map(new File("Map.txt"), tiles);
        
        // Load SDK GUI
        GUIButton[] buttons = new GUIButton[tiles.size()];
        Sprite[] tileSprites = tiles.getSprites();
        
        for (int i = 0; i < buttons.length; i++) {
            Rectangle tileRect = new Rectangle(0, i * (16 * currentXZoom + 10), 16 * currentXZoom, 16 * currentYZoom);
            
            buttons[i] = new SDKButton(tileSprites[i], tileRect, this, i);
        }
        
        GUI gui = new GUI(buttons, 5, 5, true);

        // Load objects
        gameObjects = new GameObject[MAX_GAMEOBJECTS];
        
        // Player animated sprites
        AnimatedSprite playerAnimations = new AnimatedSprite(playerSheet, 7);
        
        gameObjects[0] = gui;
        
        // Add listeners
        canvas.addKeyListener(keyListener);
        canvas.addMouseListener(mouseListener);
        canvas.addMouseMotionListener(mouseListener);
        
        // Set up the networking stuff
        networkPlayers = new ArrayList<>();
        this.mainClient = mainClient;
        
        player = new Player(playerAnimations, mainClient);
        gameObjects[gameObjects.length - 1] = player;
        
        //mainClient.startConnection();
        mainClient.setGame(this);
    }

    public void update() {
        //testRectangle.x += 1;
        for (int i = 0; i < gameObjects.length; i++) {
            if (gameObjects[i] != null){
                gameObjects[i].update(this);
                
                if (gameObjects[i] instanceof Player){
                    mainClient.sendPlayerPacket(((Player)gameObjects[i]).getAsPlayerPacket());
                }
            }
        }
    }
    
    /**
     * Finds an open space for a new gameObject in the gameObject array
     * @param go 
     */
    public void addGameObject(GameObject go){
        for (int i = 0; i < gameObjects.length; i++) {
            if (gameObjects[i] == null){
                gameObjects[i] = go;
                return;
            }
        }
        
        System.out.println("GAME OBJECT ARRAY IS FULL, TOO MANY ITEMS!");
    }

    private BufferedImage loadImage(String path) {
        try {
            BufferedImage loadedImage = ImageIO.read(Game.class.getResource(path));
            BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);
            return formattedImage;
        } catch (IOException exception) {
            //TODO: complete handling of image load exception
            exception.printStackTrace();
        }
        return null;
    }

    public void render() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
        super.paint(graphics);
        
        map.render(renderer, currentXZoom, currentYZoom);
        
        for (int i = 0; i < gameObjects.length; i++) {
            if (gameObjects[i] != null){
                gameObjects[i].render(renderer, currentXZoom, currentYZoom);
            }
        }
        
        renderer.render(graphics);

        graphics.dispose();
        bufferStrategy.show();
        renderer.clear();
    }

    public void run() {
        //BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        
        int i = 0;
        int x = 0;

        long lastTime = System.nanoTime(); //long 2^63
        double nanoSecondConversion = 1000000000.0 / 60; //60 frames per second
        double changeInSeconds = 0;
        
        int frames = 0;
        long timer = System.currentTimeMillis();

        while (true) {
            long now = System.nanoTime();

            changeInSeconds += (now - lastTime) / nanoSecondConversion;
            
            while (changeInSeconds >= 1) {
                update();
                changeInSeconds--;
            }
            
            render();
            lastTime = now;
            
            frames++;
            
            if (System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        
        
    }
    
    public KeyboardListener getKeyListener(){
        return keyListener;
    }
    
    public RenderHandler getRenderer(){
        return renderer;
    }
    
    public MouseEventListener getMouseListener(){
        return mouseListener;
    }
    
    //--------------------Map editing stuff--------------------\\
    public void handleCTRL(){
        if (keyListener.isKeyPressed('S')){
            map.saveMap();
        }
    }
    
    public void leftMousePressed(int x, int y){
        Rectangle mouseRectangle = new Rectangle(x, y, 1, 1);
        boolean clickedGUI = false;
        
        for (int i = 0; i < gameObjects.length; i++) {
            if (gameObjects[i] != null && !clickedGUI){
                clickedGUI = gameObjects[i].handleMouseClick(mouseRectangle, renderer.getCamera(), currentXZoom, currentYZoom);
            }
        }
        
        if (!clickedGUI){

            x = (int)Math.floor((x + renderer.getCamera().x) / (16.0 * currentXZoom));
            y = (int)Math.floor((y + renderer.getCamera().y) / (16.0 * currentYZoom));

            //System.out.println("X: " + x + ", Y: " + y);

            map.setTile(x, y, selectedTileID);
        }
    }
    
    public void rightMousePressed(int x, int y){
        x = (int)Math.floor((x + renderer.getCamera().x) / (16.0 * currentXZoom));
        y = (int)Math.floor((y + renderer.getCamera().y) / (16.0 * currentYZoom));
        
        //System.out.println("X: " + x + ", Y: " + y);
        
        map.removeTile(x, y);
    }
    
    public void changeTile(int tileID){
        selectedTileID = tileID;
    }
    
    public int getSelectedTileID(){
        return selectedTileID;
    }
    //-----------------End map editing stuff-------------------\\
    
    //--------------------Network connectivity stuff--------------------\\
    /**
     * Deals with incoming player packets from the server
     * @param pp The incoming player packet
     */
    public void addNetworkPlayer(PlayerPacket pp){
        if (playerExists(pp.id)){ // Update values of existing player
            getPlayer(pp.id).setNewValues(pp);
        }
        else { // Create new object for the player
            NetworkPlayer np = new NetworkPlayer(pp, this, new AnimatedSprite(playerSheet, 7));
            
            networkPlayers.add(np); // Add the player to the list of network players
            
            System.out.println("Created new player for " + pp.id);
            
            addGameObject(np); // Add the new player to the array of gameObjects
        }
    }
    
    /**
     * Remove a player from the array of gameObjects
     * @param id the ID of the network player to remove
     */
    public void removeNetworkPlayer(int id){
        for (int i = 0; i < gameObjects.length; i++) {
            if (gameObjects[i] != null && gameObjects[i] instanceof NetworkPlayer){
                if (((NetworkPlayer)gameObjects[i]).getID() == id){
                    gameObjects[i] = null; // Goodbye player!
                }
            }
        }
        
        for (int i = 0; i < networkPlayers.size(); i++) {
            if (networkPlayers.get(i).getID() == id){
                networkPlayers.remove(i);
            }
        }
    } 
    
    private boolean playerExists(int id){
        for (NetworkPlayer np : networkPlayers){
            if (np.getID() == id){
                return true;
            }
        }
        return false;
    }
    
    private NetworkPlayer getPlayer(int id){
        for (int i = 0; i < networkPlayers.size(); i++) {
            if (networkPlayers.get(i).getID() == id){
                return networkPlayers.get(i);
            }
        }
        return null;
    }

}
