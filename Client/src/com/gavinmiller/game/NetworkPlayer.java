package com.gavinmiller.game;

import com.gavinmiller.shared.FacingDirection;
import com.gavinmiller.shared.PlayerPacket;

/**
 * This class represents other players as game objects
 * @author Gavin Miller
 * @version 0.1
 */
public class NetworkPlayer implements GameObject {
    private Game game;
    private AnimatedSprite sprite;
    private int id;
    private FacingDirection facingDirection;
    private int x, y;
    private int velocityX, velocityY;
    //private Rectangle rect; // For debugging purposes
    private String name;
    
    public NetworkPlayer(PlayerPacket pp, Game game, AnimatedSprite sprite){
        this.id = pp.id;
        this.x = pp.x;
        this.y = pp.y;
        this.velocityX = pp.velocityX;
        this.velocityY = pp.velocityY;
        this.facingDirection = pp.facingDirection;
        this.sprite = sprite;
        
        //rect = new Rectangle(x, y, 16, 16);
        //rect.generateGraphics(0xFF0000FF);
        
        this.game = game;
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        renderer.renderSprite(sprite, x, y, xZoom, yZoom);
    }

    @Override
    public void update(Game game) {
        if (Math.abs(velocityX) > 0 || Math.abs(velocityY) > 0){
            sprite.update(game);
        }
        else {
            sprite.resetSprite();
        }
    }
    
    public void setNewValues(PlayerPacket pp){
        boolean didMove = false;
        //System.out.println("old x: " + x);
//        this.x = pp.x;
//        this.y = pp.y;
        this.velocityX = pp.velocityX;
        this.velocityY = pp.velocityY;
        
        x += velocityX;
        y += velocityY;
        
        //System.out.println("New x: " + x);
        
        this.facingDirection = pp.facingDirection;
        updateDirection();
        
        //    System.out.println(id + ": VelocityX: " + velocityX + ", velocityY: " + velocityY);
    }

    @Override
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
        return false;
    }
    
    public int getID(){
        return id;
    }
    
    private void updateDirection() {
        sprite.setAnimatedRange(facingDirection.getIndex() * 8, facingDirection.getIndex() * 8 + 7);
    }
}
