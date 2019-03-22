package com.gavinmiller.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Tiles class defines each tile ready for painting to a map
 * @author Gavin Miller
 * @version 0.1
 */
public class Tiles {

    private SpriteSheet spriteSheet;
    private ArrayList<Tile> tilesList = new ArrayList<>();

    // This will only work assuming the spriteSheet has been loaded
    public Tiles(File tilesFile, SpriteSheet spriteSheet) {
        this.spriteSheet = spriteSheet;

        try {
            Scanner scanner = new Scanner(tilesFile);
            while (scanner.hasNextLine()) {
                // Read each line and create a file
                String line = scanner.nextLine();
                if (!line.startsWith("//")) {
                    String[] bits = line.split(",");
                    String tileName = bits[0];
                    int x = Integer.parseInt(bits[1]);
                    int y = Integer.parseInt(bits[2]);

                    Tile tile = new Tile(tileName, spriteSheet.getSprite(x, y));

                    tilesList.add(tile);
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void renderTile(int tileID, RenderHandler renderer, int xPos, int yPos, int xZoom, int yZoom) {
        if (tileID < tilesList.size()) {
            renderer.renderSprite(tilesList.get(tileID).sprite, xPos, yPos, xZoom, yZoom);
        } else {
            System.out.println("Tile ID: " + tileID + " is not in range " + tilesList.size());
        }
    }
    
    public int size(){
        return tilesList.size();
    }
    
    public Sprite[] getSprites(){
        Sprite[] sprites = new Sprite[size()];
        for (int i = 0; i < size(); i++) {
            sprites[i] = tilesList.get(i).sprite;
        }
        
        return sprites;
    }

    class Tile {

        //public 
        public String tileName;
        public Sprite sprite;

        public Tile(String tileName, Sprite sprite) {
            this.tileName = tileName;
            this.sprite = sprite;
        }
    }
}
