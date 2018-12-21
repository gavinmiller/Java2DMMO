package com.gavinmiller.server;

import com.gavinmiller.shared.Constants;

/**
 * Main class where server is started
 * @author Gavin Miller
 * @version 0.1
 */
public class Main {
    
    private final static int TCP_PORT = Constants.PORT, UDP_PORT = Constants.PORT;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainServer server = new MainServer(TCP_PORT, UDP_PORT);
        server.startServer();
    }
    
}
