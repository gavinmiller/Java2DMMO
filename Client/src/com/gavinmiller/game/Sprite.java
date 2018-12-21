package com.gavinmiller.game;

import java.awt.image.BufferedImage;

/**
 * Sprite class for holding sprite objects
 * @author Gavin Miller
 * @version 0.1
 */
public class Sprite {

    protected int width, height;
    protected int[] pixels;

    public Sprite(SpriteSheet sheet, int startX, int startY, int width, int height) {
        this.width = width;
        this.height = height;

        pixels = new int[width * height];
        pixels = sheet.getImage().getRGB(startX, startY, width, height, pixels, 0, width);
    }

    public Sprite(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();

        pixels = new int[width * height];
        pixels = image.getRGB(0, 0, width, height, pixels, 0, width);
    }
    
    public Sprite(){
        
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getPixels() {
        return pixels;
    }
}
