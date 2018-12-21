package com.gavinmiller.game;

import java.awt.image.BufferedImage;

/**
 * Animated sprite class using array of buffered images as sprites with a speed to cycle through them
 * @author Gavin Miller
 * @version 0.1
 */
public class AnimatedSprite extends Sprite implements GameObject{

    private Sprite[] sprites;
    private int currentSprite;
    private int speed;
    private int counter;
    
    private int startSprite = 0;
    private int endSprite;
    
    /**
     * Takes array of buffered images and a speed to play at then creates an animated sprite object
     * @param images the sprites to animate
     * @param speed represents how many frames pass until the sprite changes
     */
    public AnimatedSprite(BufferedImage[] images, int speed) {
        sprites = new Sprite[images.length];
        this.endSprite = images.length - 1;
        
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new Sprite(images[i]);
        }
        
        this.currentSprite = 0;
        this.speed = speed;
        this.counter = 0;
    }
    
    /**
     * Takes a spritesheet with all the positions of each sprite frame and a speed to cycle through them, then creates an animated sprite object
     * @param sheet The sprite sheet to work with
     * @param positions The positions of each sprite in the sheet
     * @param speed The speed at which to cycle the sprites
     */
    public AnimatedSprite(SpriteSheet sheet, Rectangle[] positions, int speed){
        sprites = new Sprite[positions.length];
        this.speed = speed;
        this.endSprite = positions.length - 1;
        
        for (int i = 0; i < positions.length; i++) {
            sprites[i] = new Sprite(sheet, positions[i].x, positions[i].y, positions[i].width, positions[i].height);
        }
    }
    
    public AnimatedSprite(SpriteSheet sheet, int speed){
        sprites = sheet.getLoadedSprites();
        this.speed = speed;
        this.endSprite = sprites.length - 1;
    }
    
    /**
     * Set the range of which sprites specifically inside the object to cycle through, used for single animated sprite objects containing 
     * different variations of animations of the same sprite, i.e. run left, idle left, run right, idle right etc.
     * @param startSprite
     * @param endSprite 
     */
    public void setAnimatedRange(int startSprite, int endSprite){        
        this.startSprite = startSprite;
        this.endSprite = endSprite;
        
        if (currentSprite < startSprite || currentSprite >= endSprite){
            resetSprite();
        }
    }

    // Render is dealt specifically with the layer class
    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
    }

    @Override
    public void update(Game game) {
        if (counter >= speed){
            incrementSprite();
        }
        
        counter++;
    }
    
    @Override
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
        return false;
    }
    
    @Override
    public int getWidth(){
        return sprites[currentSprite].getWidth();
    }
    
    @Override
    public int getHeight(){
        return sprites[currentSprite].getHeight();
    }
    
    @Override
    public int[] getPixels(){
        return sprites[currentSprite].getPixels();
    }
    
    public void incrementSprite(){
        currentSprite++;
            
        if (currentSprite >= endSprite){
            currentSprite = startSprite;
        }
        
        counter = 0;
    }
    
    public void resetSprite(){
        currentSprite = startSprite;
    }
    
}
