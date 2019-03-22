package com.gavinmiller.game;

import java.awt.image.BufferedImage;

/**
 * Spritesheet class which holds a spritesheet image and can load/separate individual sprites of the same sizex and sizey as each other
 * @author Gavin Miller
 * @version 0.1
 */
public class SpriteSheet {

    private int[] pixels;
    private BufferedImage image;
    public final int SIZE_X;
    public final int SIZE_Y;
    private Sprite[] loadedSprites = null;
    private boolean spritesLoaded = false;

    private int spriteSizeX, spriteSizeY;

    public SpriteSheet(BufferedImage sheetImage) {
        image = sheetImage;

        SIZE_X = sheetImage.getWidth();
        SIZE_Y = sheetImage.getHeight();

        pixels = new int[SIZE_X * SIZE_Y];
        pixels = sheetImage.getRGB(0, 0, SIZE_X, SIZE_Y, pixels, 0, SIZE_X);
    }

    public void loadSprites(int spriteSizeX, int spriteSizeY) {
        loadSprites(spriteSizeX, spriteSizeY, 0, 0);
    }

    public void loadSprites(int spriteSizeX, int spriteSizeY, int paddingX, int paddingY) {
        this.spriteSizeX = spriteSizeX;
        this.spriteSizeY = spriteSizeY;
        loadedSprites = new Sprite[(SIZE_X / spriteSizeX) * (SIZE_Y * spriteSizeY)];
        int spriteID = 0;
        for (int y = 0; y < SIZE_Y; y += spriteSizeY + paddingY) {
            for (int x = 0; x < SIZE_X; x += spriteSizeX + paddingX) {
                loadedSprites[spriteID] = new Sprite(this, x, y, spriteSizeX, spriteSizeY);
                spriteID++;
            }
        }

        spritesLoaded = true;
    }

    public Sprite getSprite(int x, int y) {
        if (spritesLoaded) {
            int spriteID = x + y * (SIZE_X / spriteSizeX);

            if (spriteID >= loadedSprites.length) {
                System.out.println("Sprite ID: " + spriteID + " out of bounds with a max of: " + loadedSprites.length);
            } else {
                return loadedSprites[spriteID];
            }
        } else {
            System.out.println("Cannot get a sprite as they are not loaded");
        }
        return null;
    }
    
    public Sprite[] getLoadedSprites(){
        return loadedSprites;
    }

    public int[] getPixels() {
        return pixels;
    }

    public BufferedImage getImage() {
        return image;
    }
}
