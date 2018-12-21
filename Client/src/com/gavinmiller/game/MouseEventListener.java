package com.gavinmiller.game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Listener class for mouse input
 * @author Gavin Miller
 * @version 0.1
 */
public class MouseEventListener implements MouseListener, MouseMotionListener{

    private Game game;
    
    public MouseEventListener(Game game){
        this.game = game;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1){
            game.leftMousePressed(e.getX(), e.getY());
        }
        
        if (e.getButton() == MouseEvent.BUTTON3){
            game.rightMousePressed(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
}
