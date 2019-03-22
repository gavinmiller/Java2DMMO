package com.gavinmiller.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Render handler, painting images to view
 * @author Gavin Miller
 * @version 0.1
 */
public class RenderHandler {

    private BufferedImage view;
    private Rectangle camera;
    private int[] pixels;

    public RenderHandler(int width, int height) {
        //Create a BufferedImage that will represent our view.
        view = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        camera = new Rectangle(0, 0, width, height);

        //Create an array for pixels
        pixels = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();
    }

    public void render(Graphics graphics) {
        graphics.drawImage(view, 0, 0, view.getWidth(), view.getHeight(), null);
    }

    public void renderImage(BufferedImage image, int xPosition, int yPosition, int xZoom, int yZoom, boolean fixed) {
        int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        renderArray(imagePixels, image.getWidth(), image.getHeight(), xPosition, yPosition, xZoom, yZoom, fixed);
    }
    
    public void renderImage(BufferedImage image, int xPosition, int yPosition, int xZoom, int yZoom) {
        renderImage(image, xPosition, yPosition, xZoom, yZoom, false);
    }

    public void renderSprite(Sprite sprite, int xPosition, int yPosition, int xZoom, int yZoom, boolean fixed) {
        renderArray(sprite.getPixels(), sprite.getWidth(), sprite.getHeight(), xPosition, yPosition, xZoom, yZoom, fixed);
    }
    
    public void renderSprite(Sprite sprite, int xPosition, int yPosition, int xZoom, int yZoom) {
        renderSprite(sprite, xPosition, yPosition, xZoom, yZoom, false);
    }

    public void renderRectangle(Rectangle rectangle, int xZoom, int yZoom, boolean fixed) {
        int[] rectanglePixels = rectangle.getPixels();
        if (rectanglePixels != null) {
            renderArray(rectanglePixels, rectangle.width, rectangle.height, rectangle.x, rectangle.y, xZoom, yZoom, fixed);
        }
    }
    
    public void renderRectangle(Rectangle rectangle, int xZoom, int yZoom) {
        renderRectangle(rectangle, xZoom, yZoom, false);
    }
    
    public void renderRectangle(Rectangle rectangle, Rectangle offset, int xZoom, int yZoom, boolean fixed) {
        int[] rectanglePixels = rectangle.getPixels();
        if (rectanglePixels != null) {
            renderArray(rectanglePixels, rectangle.width, rectangle.height, rectangle.x + offset.x, rectangle.y + offset.y, xZoom, yZoom, fixed);
        }
    }

    //TODO: Must sort O(n*y*x*z) nested loops!
    public void renderArray(int[] renderPixels, int renderWidth, int renderHeight, int xPosition, int yPosition, int xZoom, int yZoom, boolean fixed) {
        for (int y = 0; y < renderHeight; y++) {
            for (int x = 0; x < renderWidth; x++) {
                for (int xZoomPosition = 0; xZoomPosition < xZoom; xZoomPosition++) {
                    for (int yZoomPosition = 0; yZoomPosition < yZoom; yZoomPosition++) {
                        setPixel(renderPixels[y * renderWidth + x], (x * xZoom + xPosition + xZoomPosition), (y * yZoom + yPosition + yZoomPosition), fixed);
                    }
                }
            }
        }
    }
    
    public void renderArray(int[] renderPixels, int renderWidth, int renderHeight, int xPosition, int yPosition, int xZoom, int yZoom) {
        renderArray(renderPixels, renderWidth, renderHeight, xPosition, yPosition, xZoom, yZoom, false);
    }

    private void setPixel(int pixel, int x, int y, boolean fixed) {
        int pixelIndex = 0;
        if (!fixed) {
            if (x >= camera.x && y >= camera.y && x <= camera.width + camera.x && y <= camera.height + camera.y) {
                pixelIndex = (x - camera.x) + (y - camera.y) * view.getWidth();
            }
        } else {
            if (x >= 0 || y >= 0 && x <= camera.width && y <= camera.height) {
                pixelIndex = x + y * view.getWidth();
            }
        }

        if (pixelIndex < pixels.length && pixel != Game.alpha) {
            pixels[pixelIndex] = pixel;
        }
    }

    private void setPixel(int pixel, int x, int y) {
        setPixel(pixel, x, y, false);
    }

    public Rectangle getCamera() {
        return camera;
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }
}
