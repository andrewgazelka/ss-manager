package com.simplyalec.servers.manager;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.simplyalec.servers.manager.servermanager.ServerFileManager;
import com.simplyalec.servers.manager.util.Config;
import com.simplyalec.servers.manager.util.Logger;
import com.simplyalec.servers.manager.util.SocketObject;


public class Manager {

    static private Config config;
    static private Logger logger;
    static private ServerFileManager fm;

    public static void main(String[] args) {
        disableWarning();

        logger = new Logger();
        logger.log(Logger.Level.INFO, "Starting SSManager...");

        config = new Config("/home/simplyalec/ss-manager/config.json");

        fm = new ServerFileManager("/home/simplyalec/ss-manager/servers");

        //Setup Socket
        Configuration sIOConfig = new Configuration();
        sIOConfig.setHostname("localhost");
        sIOConfig.setPort(config.getInt("port"));
        sIOConfig.setAuthorizationListener(new AuthorizationListener() {
            @Override
            public boolean isAuthorized(HandshakeData data) {
                String token = data.getSingleUrlParam("token");
                if(token == null) //They might not even have a token.
                    return false;
                return token.equals(config.getString("token"));
            }
        });
        final SocketIOServer server = new SocketIOServer(sIOConfig);
        server.addEventListener("chatevent", SocketObject.class, new DataListener<SocketObject>() {
            @Override
            public void onData(SocketIOClient client, SocketObject data, AckRequest ackRequest) {
                // broadcast messages to all clients
                server.getBroadcastOperations().sendEvent("chatevent", data);
            }
        });

        server.start();
        logger.log(Logger.Level.INFO, "Hosting Socket on 127.0.0.1:" + config.getInt("port") + ".");

    }
    /*
     * See https://stackoverflow.com/questions/46454995/how-to-hide-warning-illegal-reflective-access-in-java-9-without-jvm-argument, https://github.com/netty/netty/issues/7254
     * This is a dumb issue in netty, we can ignore it.
     */
    public static void disableWarning() {
        System.err.close();
        System.setErr(System.out);
    }
}
