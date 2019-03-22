package com.gavinmiller.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gavinmiller.shared.AuthorisationPacket;
import com.gavinmiller.shared.Constants;
import com.gavinmiller.shared.LoginPacket;
import com.gavinmiller.shared.PlayerPacket;
import java.io.IOException;
import javax.swing.JOptionPane;
import com.gavinmiller.game.Game;

/**
 * Main client class for the client project, deals with server to and from
 * @author Gavin Miller
 * @version 0.1
 */
public class MainClient {
    private Client client; // Kryonet client
    private Kryo kryo;
    private int tcpPort = Constants.PORT, udpPort = Constants.PORT;
    private String ipAddress = Constants.SERVER_IP;
    private Game game;
    private boolean gameRunning;
    private boolean loggingIn;
    
    private final int TIMEOUT_TIME = 5000;
    private LoginGUI lGUI;
    
    public MainClient(){
        loggingIn = false;
        gameRunning = false;
    }
    
    public MainClient(String ipAddress, int tcpPort, int udpPort){
        this.ipAddress = ipAddress;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
    }
    
    public void setLGUI(LoginGUI lGUI){
        this.lGUI = lGUI;
        this.loggingIn = true;
    }
    
    public void setGame(Game game){
        this.game = game;
        gameRunning = true;
    }
    
    public void startConnection(){
        client = new Client();
        
       new Thread(client).start();
        
        startAndRegisterKryo();
        
        try {
            client.connect(TIMEOUT_TIME, ipAddress, tcpPort, udpPort);
            
            System.out.println("Client connected!");
            
            client.addListener(new Listener(){
                @Override
                public void connected(Connection con){
                    System.out.println("connected");
                }
                @Override
                public void received(Connection con, Object obj){
                    if (loggingIn){
                        if (obj instanceof AuthorisationPacket){
                            AuthorisationPacket ap = (AuthorisationPacket)obj;
                            
                            if (ap.authorised){
                                System.out.println("Success!");
                                lGUI.loginSuccess();
                                loggingIn = false;
                            }
                            else {
                                System.out.println("Failed...");
                                lGUI.loginFailed();
                            }
                        }
                    }
                    else if (gameRunning){
                        if (obj instanceof PlayerPacket){
                            PlayerPacket pp = (PlayerPacket)obj;
                            if (pp.connected){
                                game.addNetworkPlayer(pp);
                            }
                            else {
                                game.removeNetworkPlayer(pp.id);
                            }
                        }
                    }
                }
                @Override
                public void disconnected(Connection con){
                    System.out.println("Disconnected from server!");
                }
            });
        } catch (IOException ex) {
            System.out.println("Exception occurred: " + ex.getMessage());
            
            if (loggingIn){
                JOptionPane.showMessageDialog(lGUI, "Cannot connect to the server");
                if (JOptionPane.showConfirmDialog(lGUI, "Try again?") == 0){
                    startConnection();
                }
                else {
                    System.exit(0);
                }
            }
        }
    }
    
    public void requestLogin(String username, String password){
        loggingIn = true;
        LoginPacket lp = new LoginPacket();
        lp.setUsername(username);
        lp.setPassword(password);
        client.sendTCP(lp);
    }
    
    public void sendPlayerPacket(PlayerPacket pp){
        client.sendTCP(pp);
    }
    
    private void startAndRegisterKryo(){
        kryo = client.getKryo();
        
        Constants.registerKryo(kryo);
        //kryo.register(SimpleMessage.class);
        //kryo.register(PacketMessage.class);
    }
}
