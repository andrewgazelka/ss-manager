package com.simplyalec.servers.manager;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.simplyalec.servers.manager.util.Config;
import com.simplyalec.servers.manager.util.Logger;

public class Manager {

    static private Config config;
    static private Logger logger;

    public static void main(String[] args) {
        logger = new Logger();
        logger.log(Logger.Level.INFO, "Starting SSManager...");

        config = new Config("/home/simplyalec/config.json");

        //Setup Socket
        Configuration sIOConfig = new Configuration();
        sIOConfig.setHostname("localhost");
        sIOConfig.setPort(9092);
        sIOConfig.setAuthorizationListener(new AuthorizationListener() {
            @Override
            public boolean isAuthorized(HandshakeData data) {
                String token = data.getSingleUrlParam("token");
                return token.equals(config.getValue("token"));
            }
        });
    }
}
