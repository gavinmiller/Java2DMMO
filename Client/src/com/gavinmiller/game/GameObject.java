package com.gavinmiller.game;

/**
 * GameObject interface for representing physical objects in the game world.
 * Might make abstract class instead...
 * @author Gavin Miller
 * @version 0.1
 */
public interface GameObject {
    public void render(RenderHandler renderer, int xZoom, int yZoom);
    
    public void update(Game game);
    
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom);
}
