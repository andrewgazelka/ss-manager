package com.simplyalec.servers.manager;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.simplyalec.servers.manager.servermanager.ServerFileManager;
import com.simplyalec.servers.manager.util.Config;
import com.simplyalec.servers.manager.util.Logger;
import com.simplyalec.servers.manager.util.SocketObject;

import java.nio.file.Path;
import java.nio.file.Paths;


public class Manager {

    private final Config config;
    private final Logger logger;
    private final ServerFileManager fileManager;

    private static Manager instance;
    private final Path baseDirectory;
    private final Path configPath;
    private final Path serverPath;

    public Manager() {
        disableWarning();

        logger = new Logger();
        logger.log(Logger.Level.INFO, "Starting SSManager...");

        String homeDir = System.getProperty("user.home");

        // Create hidden directory
        baseDirectory = Paths.get(homeDir, ".ss-manager");

        configPath = baseDirectory.relativize(Paths.get("config.json"));
        config = new Config(configPath);

        serverPath = baseDirectory.relativize(Paths.get("servers"));
        fileManager = new ServerFileManager(serverPath);

        //Setup Socket
        Configuration sIOConfig = new Configuration();
        sIOConfig.setHostname("localhost");
        sIOConfig.setPort(config.getInt("port"));
        sIOConfig.setAuthorizationListener(new AuthorizationListener() {
            @Override
            public boolean isAuthorized(HandshakeData data) {
                String token = data.getSingleUrlParam("token");
                if (token == null) //They might not even have a token.
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

    public static void main(String[] args) {
        instance = new Manager();
    }

    public static Manager getInstance() {
        return instance;
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
