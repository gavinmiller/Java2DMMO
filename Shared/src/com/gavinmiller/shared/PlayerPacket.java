package com.gavinmiller.shared;

/**
 * Player info needed to be sent over the network such as location etc...
 * @author Gavin Miller
 * @version 0.1
 */
public class PlayerPacket {
    
    public int id;

    public int x, y;
    public int velocityX, velocityY;
    public FacingDirection facingDirection;
    
    public boolean connected;

    /**
     * Must always have a parameterless constructor when registering classes with Kryo
     */
    public PlayerPacket(){
        
    }
    
    /**
     * Used to broadcast when a certain player has disconnected
     * @param id The current network id of that player
     */
    public PlayerPacket(int id){
        this.id = id;
        connected = false;
    }
    
    public PlayerPacket(int x, int y){
        this.x = x;
        this.y = y;
        this.facingDirection = FacingDirection.DOWN;
        connected = true;
    }
    
    public PlayerPacket(int x, int y, int velocityX, int velocityY, FacingDirection facingDirection){
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.facingDirection = facingDirection;
        connected = true;
    }
}
