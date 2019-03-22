package com.gavinmiller.main;

import static javax.swing.SwingUtilities.invokeLater;
import com.gavinmiller.client.LoginGUI;
import com.gavinmiller.client.MainClient;
import com.gavinmiller.game.Game;

/**
 * Main class for client and game operations
 * @author Gavin Miller
 * @version 0.1
 */
public class Main {
    private static MainClient mainClient;
    private static Game game;
    private static LoginGUI lGUI;
    
    public static void main(String[] args){
        
        mainClient = new MainClient();
        
        
        
        Runnable loginGUI = new Runnable(){
            @Override
            public void run() {
                lGUI = new LoginGUI(mainClient);
                mainClient.setLGUI(lGUI);
                mainClient.startConnection();
                lGUI.setVisible(true);
            }
            
        };
        
        invokeLater(loginGUI);
    }
    
    // When login is validated start game
    //TODO: need to add User variables to pass in when user is loaded
    public static void startGame(){
        game = new Game(mainClient);
        Thread gameThread = new Thread(game);
        gameThread.start();
    }
}
