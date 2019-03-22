package com.gavinmiller.game;

import com.gavinmiller.shared.FacingDirection;
import com.gavinmiller.shared.PlayerPacket;
import com.gavinmiller.client.MainClient;

/**
 * Player class
 * @author Gavin Miller
 * @version 0.1
 */
public class Player implements GameObject {
    private AnimatedSprite sprite;
    private MainClient mainClient;

    private int x, y;
    private int velocityX, velocityY;

    private int speed = 7;

    private char keyCodeUp, keyCodeDown, keyCodeLeft, keyCodeRight;

    private FacingDirection facingDirection;
    private boolean didMove;

    public Player(AnimatedSprite sprite, MainClient client) {
        this.sprite = sprite;

        keyCodeUp = 'W';
        keyCodeDown = 'S';
        keyCodeLeft = 'A';
        keyCodeRight = 'D';

        x = 0;
        y = 0;
        velocityX = 0;
        velocityY = 0;

        facingDirection = FacingDirection.DOWN;
        didMove = false;
        updateDirection();
        
        this.mainClient = client;
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        renderer.renderSprite(sprite, x, y, xZoom, yZoom);
    }

    @Override
    public void update(Game game) {
        KeyboardListener keyListener = game.getKeyListener();

        velocityX = 0;
        velocityY = 0;
        didMove = false;
        
        FacingDirection newDirection = facingDirection;

        if (keyListener.isKeyPressed(keyCodeLeft)) {
            velocityX = -speed;
            facingDirection = FacingDirection.LEFT;
            didMove = true;
        }

        if (keyListener.isKeyPressed(keyCodeRight)) {
            velocityX = speed;
            facingDirection = FacingDirection.RIGHT;
            didMove = true;
        }

        if (keyListener.isKeyPressed(keyCodeUp)) {
            velocityY = -speed;
            facingDirection = FacingDirection.UP;
            didMove = true;
        }

        if (keyListener.isKeyPressed(keyCodeDown)) {
            velocityY = speed;
            facingDirection = FacingDirection.DOWN;
            didMove = true;
        }

        //if (Math.abs(velocityX) + Math.abs(velocityY) > speed){
        //sortDiagonalSpeed();
        //} //TODO: Sort the diagonal speed boost
        if (facingDirection != newDirection){
            updateDirection();
            sprite.resetSprite();
        }

        x += velocityX;
        y += velocityY;

        if (didMove) {
            sprite.update(game);
            if (mainClient == null){
                System.out.println("Null client");
                //TODO: ?
            }
        } 
        else {
            sprite.resetSprite();
        }

        updateCamera(game.getRenderer().getCamera());
    }
    
    @Override
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
        return false;
    }

    public void updateCamera(Rectangle camera) {
        camera.x = x - (camera.width / 2);
        camera.y = y - (camera.height / 2);
    }

    private void sortDiagonalSpeed() {
        velocityX /= (float) Math.sqrt(2);
        velocityY /= (float) Math.sqrt(2);
    }

    private void updateDirection() {
        sprite.setAnimatedRange(facingDirection.getIndex() * 8, facingDirection.getIndex() * 8 + 7);
    }
    
    public PlayerPacket getAsPlayerPacket(){
        PlayerPacket pp = new PlayerPacket(x, y, velocityX, velocityY, facingDirection);
        
        return pp;
    }

}
