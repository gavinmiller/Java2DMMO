package com.gavinmiller.shared;

/**
 * Login packet used when inputting username and password
 * @author Gavin Miller
 * @version 0.1
 */
public class LoginPacket {
    private String username;
    private String password;
    
    public LoginPacket(){
        
    }

    /**
     * @return the userName input
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password input
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param username setting the username entered
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @param password setting the password entered
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
