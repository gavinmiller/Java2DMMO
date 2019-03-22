package com.gavinmiller.shared;

/**
 * Separated from the player class, translates the direction the player is facing in order to change the player sprite
 * @author Gavin Miller
 * @version 0.1
 */
public enum FacingDirection {
    RIGHT(0),
    LEFT(1),
    UP(2),
    DOWN(3);

    private int index;
    
    private FacingDirection(){
        this.index = 0;
    }

    private FacingDirection(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
