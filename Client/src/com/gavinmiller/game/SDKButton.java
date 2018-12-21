package com.gavinmiller.game;

/**
 * SDKButton class defines a GUI button for painting tiles to the map
 * @author Gavin Miller
 * @version 0.1
 */
public class SDKButton extends GUIButton {
    
    private Game game;
    private int tileID;
    private Rectangle offsetRect = null;
    private boolean isGreen;
    

    public SDKButton(Sprite tileSprite, Rectangle rect, Game game, int tileID) {
        super(tileSprite, rect, true);
        this.game = game;
        this.tileID = tileID;
        
        rect.generateGraphics(0xFFD83D);
        isGreen = false;
    }
    
    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom, Rectangle interfaceRect){
        renderer.renderRectangle(rect, interfaceRect, 1, 1, fixed);
        renderer.renderSprite(sprite, 
                rect.x + interfaceRect.x + (xZoom - (xZoom - 1)) * rect.width / 2 / xZoom, 
                rect.y + interfaceRect.y + (yZoom - (yZoom - 1)) * rect.height / 2 / yZoom, 
                xZoom - 1, 
                yZoom - 1, 
                fixed);
    }
    
    @Override
    public void update(Game game) {
        if (!isGreen && game.getSelectedTileID() == tileID){
            rect.generateGraphics(0xA1FF3D);
            isGreen = true;
        }
        else if (isGreen && game.getSelectedTileID() != tileID){
            rect.generateGraphics(0xFFD83D);
            isGreen = false;
        }
    }

    @Override
    public void activate() {
        game.changeTile(tileID);
    }
    
}
