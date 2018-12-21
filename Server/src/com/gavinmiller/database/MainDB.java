package com.gavinmiller.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.gavinmiller.userdata.Player;

/**
 * Main class for interacting with the database
 * @author Gavin Miller
 * @version 0.1
 */
public class MainDB implements Runnable{
    // Basic db stuff
    private static final String serverAddress = "localhost";
    private static final String serverPort = "52216";
    private static final String dbName = "mmo_db";
    
    private static final String dbUsername = "mainserver";
    private static final String dbPassword = "aZhEjj11zk23455";
    
    // Account stuff
    private static final String accountsTableName = "Accounts";
    
    // Character stuff
    private static final String charactersTableName = "Characters";
    
    // Connection/DB stuff
    private Connection dbConnection;
    
    private final String getConnectionURL(){
        // Return the first line instead if you are not using MSSQL with a windows authenticated user
        //return "jdbc:sqlserver://" + serverAddress + ":" + serverPort + ";databaseName=" + dbName + ";user=" + dbUsername + ";password=" + dbPassword;
        return "jdbc:sqlserver://" + serverAddress + ":" + serverPort + ";databaseName=" + dbName + ";integratedSecurity=true";
    }

    @Override
    public void run() {
        beginStream();
    }
    
    private void beginStream(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            dbConnection = DriverManager.getConnection(getConnectionURL());
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
    
    public boolean checkLogin(String username, String password){
        
        String loginQuery = "SELECT 1 FROM " + accountsTableName + " WHERE player_username = \'" + username + "\' AND player_password = \'" + password + "\'";
        
        try {
            PreparedStatement pstmt = dbConnection.prepareStatement(loginQuery);
            ResultSet rs = pstmt.executeQuery();
            
            // If there is a player with that user and pass, next check if they are connected, otherwise return false
            if (rs.next()){
                return isPlayerConnected(username);
            }
        } catch (SQLException ex) {
            //TODO: Handle sql exception
            ex.printStackTrace();
            return false;
        }
        
        return false;
    }
    
    private boolean isPlayerConnected(String username){
        String isConnectedQuery = "SELECT is_connected FROM " + charactersTableName + " C, " + accountsTableName + " A WHERE (A.player_username = \'" + username + "\' AND A.player_id = C.player_id)";
        
        try {
            PreparedStatement pstmt = dbConnection.prepareStatement(isConnectedQuery);
            ResultSet rs = pstmt.executeQuery();
            
            // If there is a player with that user and pass, next check if they are connected
            if (rs.next()){
                return !rs.getBoolean("is_connected");
            }
        } catch (SQLException ex) {
            //TODO: Handle sql exception
            ex.printStackTrace();
            return false;
        }
        
        return false;
    }
    
    public Player getPlayer(String username){
        Player player = null;
        
        String playerDetailsQuery = "SELECT C.* FROM " + charactersTableName + " C, " + accountsTableName + " A WHERE (A.player_username = \'" + username + "\' AND A.player_id = C.player_id)";
        
        try {
            PreparedStatement pstmt = dbConnection.prepareStatement(playerDetailsQuery);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()){
                int playerID = rs.getInt("player_id");
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                boolean connected = rs.getBoolean("is_connected");
                
                player = new Player(playerID, username, x, y, connected);
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            //TODO: Handle sql exception
            ex.printStackTrace();
        }
        
        return player;
    }
    
    public void changeConnectedPlayer(boolean isConnected, int playerID){
        String updateConnectedPlayerQuery = "UPDATE " + charactersTableName + " SET is_connected = " + (isConnected ? 1 : 0) + " WHERE player_id = " + playerID;
        
        try {            
            dbConnection.createStatement().execute(updateConnectedPlayerQuery);
        } catch (SQLException ex) {
            //TODO: Handle sql exception
            ex.printStackTrace();
        }
    }
    
}
