package com.gavinmiller.game;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * KeyboardListener class that changes a list of booleans dynamically when keys are pressed and released.
 * @author Gavin Miller
 * @version 0.1
 */
public class KeyboardListener implements KeyListener, FocusListener{

    private boolean[] keys = new boolean[120];
    
    private Game game;
    
    public KeyboardListener(Game game){
        this.game = game;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println((char)e.getKeyCode());
        
        int keyCode = e.getKeyCode();
        
        if (keyCode < keys.length){
            keys[keyCode] = true;
        }
        
        if (keys[KeyEvent.VK_CONTROL]){
            game.handleCTRL();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if (keyCode < keys.length){
            keys[keyCode] = false;
        }
    }

    /**
     * Sets all keys pressed to false when user switches back on to window
     * @param e 
     */
    @Override
    public void focusGained(FocusEvent e) {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
    }

    /**
     * Sets all keys pressed to false when user changes window
     * @param e 
     */
    @Override
    public void focusLost(FocusEvent e) {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
    }
    
    public boolean isKeyPressed(char keyChar){
        return isKeyPressed((int)keyChar);
    }
    
    public boolean isKeyPressed(int keyCode){
        
        if (keyCode < keys.length){
            //System.out.println(keys[keyCode]);
            return keys[keyCode];
        }
        
        System.out.println("That key is not registered in the keyboard listener!!!");
        return false;
    }
    
}
