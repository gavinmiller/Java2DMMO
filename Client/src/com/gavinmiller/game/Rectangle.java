package com.gavinmiller.game;

/**
 * Rectangle class defining rectangles.
 * @author Gavin Miller
 * @version 0.1
 */
public class Rectangle {

    public int x, y, width, height;
    private int[] pixels;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle() {
        this(0, 0, 0, 0);
    }

    public void generateGraphics(int colour) {
        pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x + y * width] = colour;
            }
        }
    }

    public void generateGraphics(int borderWidth, int colour) {
        pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if ((x <= borderWidth || y <= borderWidth) || (x >= width - borderWidth || y >= (height - borderWidth))) {
                    pixels[x + y * width] = colour;
                } else {
                    pixels[x + y * width] = Game.alpha;
                }
            }
        }
    }
    
    public boolean intersects(Rectangle otherRectangle){
        if (x > otherRectangle.x + otherRectangle.width || otherRectangle.x > x + width){
            return false;
        }
        
        if (y > otherRectangle.y + otherRectangle.height || otherRectangle.y > y + height){
            return false;
        }
        
        return true;
    }

    public int[] getPixels() {

        if (pixels == null) {
            System.out.println("Attempted to retrieve pixels from a rectangle without generated graphics...");
            return null;
        }

        return pixels;
    }
    
    @Override
    public String toString(){
        return "[" + x + ", " + y + ", " + width + ", " + height + "]";
    }

}
