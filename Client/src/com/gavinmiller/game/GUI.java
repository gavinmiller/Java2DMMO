package com.gavinmiller.game;

/**
 * GUI class for displaying GUI objects not within the game world but directly on the camera
 * @author Gavin Miller
 * @version 0.1
 */
public class GUI implements GameObject {
    
    private Sprite backgroundSprite;
    private GUIButton[] buttons;
    private boolean fixed;
    private Rectangle rect;
    
    public GUI(Sprite backgroundSprite, GUIButton[] buttons, int x, int y, boolean fixed){
        this.backgroundSprite = backgroundSprite;
        this.buttons = buttons;
        this.fixed = fixed;
        
        rect = new Rectangle();
        rect.x = x;
        rect.y = y;
        
        if (backgroundSprite != null){
            rect.width = backgroundSprite.getWidth();
            rect.height = backgroundSprite.getHeight();
        }
    }
    
    public GUI(GUIButton[] buttons, int x, int y, boolean fixed){
        this(null, buttons, x, y, fixed);
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        if (backgroundSprite != null){
            renderer.renderSprite(backgroundSprite, rect.x, rect.y, xZoom, yZoom, fixed);
        }
        
        if (buttons != null){
            
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].render(renderer, xZoom, yZoom, rect);
            }
        }
    }

    @Override
    public void update(Game game) {
        if (buttons != null){
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].update(game);
            }
        }
    }

    @Override
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {

        if (!fixed){
            mouseRectangle = new Rectangle(mouseRectangle.x + camera.x, mouseRectangle.y + camera.y, 1, 1);
        }
        else {mouseRectangle = new Rectangle(mouseRectangle.x, mouseRectangle.y, 1, 1);
            
        }
        
        if (rect.width == 0 || rect.height == 0 || mouseRectangle.intersects(rect)){
            mouseRectangle.x -= rect.x;
            mouseRectangle.y -= rect.y;
            for (int i = 0; i < buttons.length; i++) {          
                if (buttons[i].handleMouseClick(mouseRectangle, camera, xZoom, yZoom)){
                    return true;
                }
            }
        }
        return false;
    }
    
}
