package com.gavinmiller.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.gavinmiller.shared.AuthorisationPacket;
import com.gavinmiller.shared.Constants;
import com.gavinmiller.shared.LoginPacket;
import com.gavinmiller.shared.PlayerPacket;
import com.gavinmiller.database.MainDB;
import java.io.IOException;
import java.util.ArrayList;
import com.gavinmiller.userdata.Player;

//TODO: Create GUI to host servers with different ips/ports and info etc
/**
 * Main server class where the server will be hosted
 * @author Gavin Miller
 * @version 0.1
 */
public class MainServer {
    private final int MAX_CLIENTS = Constants.MAX_PLAYERS;
    
    private Server server;
    private ArrayList<Connection> clients;
    private Kryo kryo;
    private final int tcpPort, udpPort;
    
    private MainDB mainDB;
    
    private Player[] connectedPlayers;
    
    public MainServer(int tcpPort, int udpPort){
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        clients = new ArrayList<>();
        connectedPlayers = new Player[MAX_CLIENTS];
        
        mainDB = new MainDB();
        new Thread(mainDB).start();
        
        
    }
    
    public void startServer(){
    
        System.out.println("Starting the server...");
        
        server = new Server();
        
        try {
            server.start();
            
            kryo = server.getKryo();
            
            server.bind(tcpPort, udpPort);
            
            registerClasses();
        
            System.out.println("Server has started...");
            
            server.addListener(new Listener() {
                @Override
                public void connected(Connection con){
                    System.out.println("Connection received from: " + con.getRemoteAddressTCP());
                    
                    if (clients.size() < MAX_CLIENTS){
                        clients.add(con);
                    }
                    else {
                        System.out.println("Server full...");
                    }
                }
                @Override
                public void received (Connection con, Object obj){
                    if (obj instanceof PlayerPacket){
                        PlayerPacket pp = (PlayerPacket)obj;
                        pp.id = con.getID();
                        broadcastPlayerPacket(pp.id, pp);
                        //System.out.println("sending pp");
                    }
                    else if (obj instanceof LoginPacket){
                        LoginPacket lp = (LoginPacket)obj;
                        AuthorisationPacket ap = new AuthorisationPacket();
                        
                        ap.authorised = mainDB.checkLogin(lp.getUsername(), lp.getPassword());
                        
                        if (ap.authorised){
                            Player tempPlayer = mainDB.getPlayer(lp.getUsername());
                            tempPlayer.setCurrentID(con.getID());
                            addPlayer(tempPlayer);
                        }
                        
                        con.sendTCP(ap);
                    }
                }
                @Override
                public void disconnected(Connection con){
                    System.out.println("Client: " + con.getID() + " has disconnected...");
                    removePlayer(con.getID());
                    removeClient(con);
                    broadcastPlayerPacket(-1, new PlayerPacket(con.getID()));
                }
            });
            
            
        } catch (IOException ex) {
            System.out.println("Problem binding ports...");
            System.out.println("Exception: " + ex.getMessage());
        }
    }
    
    private void registerClasses(){
        Constants.registerKryo(kryo);
        //kryo.register(SimpleMessage.class);
    }
    
    private void removeClient(Connection con){
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getID() == con.getID()){
                clients.remove(i);
                return;
            }
        }
        
        System.out.println("Couldn't find that client??");
    }
    
    private void broadcastPlayerPacket(int sendingClient, PlayerPacket packet){
        for (Connection con : clients){
            if (con.getID() != sendingClient){
                con.sendTCP(packet);
            }
        }
    }
    
    private void addPlayer(Player player){
        for (int i = 0; i < connectedPlayers.length; i++) {
            if (connectedPlayers[i] == null){
                connectedPlayers[i] = player;
                mainDB.changeConnectedPlayer(true, player.getID());
                return;
            }
            
            if (connectedPlayers[i].getUsername().equals(player.getUsername())){
                connectedPlayers[i] = player; // Overwrite in case of disconnection but not deleted then reconnection
                mainDB.changeConnectedPlayer(true, player.getID());
                return;
            }
        }
        
        System.out.println("Server is full!");
    }
    
    private void removePlayer(int currentID){
        for (int i = 0; i < connectedPlayers.length; i++) {
            if (connectedPlayers[i] != null && connectedPlayers[i].getCurrentID() == currentID){
                mainDB.changeConnectedPlayer(false, connectedPlayers[i].getID());
                connectedPlayers[i] = connectedPlayers[getLastPlayerIndex()];
                connectedPlayers[i] = null;
            }
        }
    }
    
    private int getLastPlayerIndex(){
        int index = 0;
        
        for (int i = 0; i < connectedPlayers.length; i++) {
            if (connectedPlayers[i] != null){
                index = i;
            }
        }
        
        return index;
    }
}
