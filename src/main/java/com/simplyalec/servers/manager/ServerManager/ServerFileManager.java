package com.simplyalec.servers.manager.servermanager;

import com.simplyalec.servers.manager.util.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ServerFileManager {

    private JSONParser parser;
    private BufferedReader serversReader;
    private BufferedWriter serversWriter;
    private Logger logger;
    private File serverFolder;
    private Path configFolder;
    private Path serversFile;
    private List<ServerConfig> loadedConfigs; // Use List<> instead of ArrayList<> for more abstraction
    private List<Server> servers;
    private JSONObject serversJSON;

    public ServerFileManager(Path serversPath) {

        // TODO: a lot of the same code as Config. Let's fix this.
        logger = new Logger();
        loadedConfigs = new ArrayList<>();
        servers = new ArrayList<>();

        try {


            Files.createDirectories(serversPath);

            configFolder = serversPath.resolve("configs");
            Files.createDirectories(configFolder);

            serversFile = serversPath.resolve("server.json");

            parser = new JSONParser();

            if (!Files.exists(serversFile)) {
                logger.log(Logger.Level.WARN, "Config not found. Generating one for you!");
                Files.createFile(serversFile);
                writeDefaultServers();
            }

            if (!Files.isReadable(serversFile)) {
                logger.log(Logger.Level.ERR, String.format("Config file {%s} is not readable. Try recreating it", serversFile.toAbsolutePath()));
                System.exit(0);
            }

            serversReader = Files.newBufferedReader(serversFile);
            serversWriter = Files.newBufferedWriter(serversFile);

            serversJSON = (JSONObject) parser.parse(serversReader);

            if (verifyServers()) {
                logger.log(Logger.Level.DEBUG, "Loaded servers.json!");
            } else {
                logger.log(Logger.Level.ERR, "servers.json can not be verified. Try making sure the file is readable and has the correct syntax.");
                System.exit(0);
            }

            loadServers();
            loadConfigs();
        } catch (Exception ex) { //Just catch them all.
            ex.printStackTrace();
            logger.log(Logger.Level.ERR, "Failed to read/modify /servers/ folder. Check directory permissions.");
            System.exit(0);
        }
    }

    public void close() throws IOException {
        serversWriter.close();
        serversReader.close();
    }

    private void createNew(String name) {

    }

    private ServerConfig findConfig(String name) {
        for (ServerConfig config : loadedConfigs) {
            if (config.getName().equals(name))
                return config;
        }
        return null;
    }

    private void writeDefaultServers() throws IOException {
        JSONObject servers = new JSONObject();
        servers.put("servers", new JSONArray());

        serversWriter.write(servers.toJSONString());
    }

    private boolean verifyServers() {
        return true;

    }

    private void loadServers() throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        JSONObject obj = (JSONObject) parser.parse(serversReader);
        serversJSON = obj;
        JSONArray serversArr = (JSONArray) obj.get("servers");

        for (int i = 0; i < serversArr.size(); i++) {
            servers.add(new Server(new File(serverFolder.getAbsoluteFile() + "/" + serversArr.get(i))));
            logger.log(Logger.Level.INFO, "Loaded server " + serversArr.get(i) + ".");
        }
    }

    private void loadConfigs() {
        try {
            Files
                    .list(configFolder)
                    .forEach(child -> {
                        JSONParser parser = new JSONParser();
                        BufferedReader reader = null;
                        try {
                            reader = Files.newBufferedReader(child);
                            JSONObject config = (JSONObject) parser.parse(reader);

                            if (config.get("name") == null || config.get("launchOptions") == null || config.get("url") == null || config.get("stopCommand") == null || config.get("start") == null) {
                                logger.log(Logger.Level.WARN, "Invalid config file '" + child.toAbsolutePath() + "'. Skipping.");
                                reader.close();
                                return;
                            }

                            loadedConfigs.add(new ServerConfig(config));
                            logger.log(Logger.Level.INFO, "Loaded server config '" + child.toAbsolutePath() + "'.");
                            reader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.log(Logger.Level.WARN, "Failed to read config '" + child.toAbsolutePath() + "'. Skipping.");
                            return;
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
