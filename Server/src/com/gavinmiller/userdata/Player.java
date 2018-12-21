package com.gavinmiller.userdata;

/**
 * Player class to convert players from db to changeable objects on server side
 * @author Gavin Miller
 * @version 0.1
 */
public class Player {
    private final String username;
    private final int id;
    private int currentID;
    private int x, y;
    //private FacingDirection facingDirection; //?
    private boolean connected;
    // Sprite?
    // EXP? etc
    
    public Player(int id, String username, int x, int y, boolean connected){
        this.id = id;
        this.username = username;
        this.x = x;
        this.y = y;
        this.connected = connected;
    }
    
    public String getUsername(){
        return username;
    }
    
    public boolean getConnected(){
        return connected;
    }
    
    public int getID(){
        return id;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public int getCurrentID(){
        return currentID;
    }
    
    public void setX(int newX){
        x = newX;
    }
    
    public void setY(int newY){
        y = newY;
    }
    
    public void setCurrentID(int newID){
        currentID = newID;
    }
}
