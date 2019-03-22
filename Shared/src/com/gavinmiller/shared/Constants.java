package com.gavinmiller.shared;

import com.esotericsoftware.kryo.Kryo;

/**
 * Constant fields and methods used by both the client and server
 * @author Gavin Miller
 * @version 0.1
 */
public class Constants {
    public static final String SERVER_IP = "127.0.0.1";
    public static final int PORT = 27960;
    public static final int MAX_PLAYERS = 32;
    
    /**
     * Registers the shared classes with the Kryo for sending over the network
     * @param kryo The Kryo to register classes for
     */
    public static final void registerKryo(Kryo kryo){
        kryo.register(FacingDirection.class);
        kryo.register(PlayerPacket.class);
        kryo.register(LoginPacket.class);
        kryo.register(AuthorisationPacket.class);
    }
}
