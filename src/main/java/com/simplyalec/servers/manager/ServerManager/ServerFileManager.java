package com.simplyalec.servers.manager.ServerManager;

import com.simplyalec.servers.manager.util.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ServerFileManager {

    private Logger logger;
    private File serverFolder;
    private File configFolder;
    private File serversFile;
    private ArrayList<ServerConfig> loadedConfigs;
    private ArrayList<Server> servers;
    private JSONObject serversJSON;

    public ServerFileManager(String filePath) {
        logger = new Logger();
        loadedConfigs = new ArrayList<>();
        servers = new ArrayList<>();

        try {
            File serversFolder = new File(filePath);
            this.serverFolder = serversFolder;
            if(serversFolder.exists() && serversFolder.isDirectory()){
            }else{
                serversFolder.mkdir();
            }
            File configs = new File(this.serverFolder.getAbsolutePath() + "/configs/");
            configFolder = configs;
            if(!(configs.exists() && configs.isDirectory())){
                configs.mkdir();
            }
            File servers = new File(this.serverFolder.getAbsolutePath() + "/servers.json");
            serversFile = servers;
            if (servers.isFile() && servers.canRead() && verifyServers()) {
                logger.log(Logger.Level.DEBUG, "Loaded servers.json!");
            } else {
                logger.log(Logger.Level.DEBUG, "Creating empty servers.json file.");
                writeDefaultServers();
            }
            JSONParser parser = new JSONParser();
            serversJSON = (JSONObject)parser.parse(new FileReader(this.serversFile));
            loadServers();
            loadConfigs();
        } catch (Exception ex) { //Just catch them all.
            ex.printStackTrace();
            logger.log(Logger.Level.ERR, "Failed to read/modify /servers/ folder. Check directory permissions.");
            System.exit(0);
        }
    }

    private void createNew(String name){

    }

    private ServerConfig findConfig(String name){
        for(ServerConfig config :loadedConfigs){
            if(config.getName().equals(name))
                return config;
        }
        return null;
    }

    private void writeDefaultServers() throws IOException{
        JSONObject servers = new JSONObject();
        servers.put("servers", new JSONArray());
        FileWriter file = new FileWriter(serversFile);
        file.write(servers.toJSONString());
        file.close();

    }

    private boolean verifyServers() throws IOException {
        JSONParser parser = new JSONParser();
        try {
            JSONObject test = (JSONObject)parser.parse(new FileReader(serversFile));
        }catch (ParseException e){
            return false;
        }
        return true;

    }

    private void loadServers() throws IOException, ParseException{
        JSONParser parser = new JSONParser();

        JSONObject obj = (JSONObject) parser.parse(new FileReader(serversFile));
        serversJSON = obj;
        JSONArray serversArr = (JSONArray)obj.get("servers");

        for (int i = 0; i < serversArr.size(); i++) {
            servers.add(new Server(new File(serverFolder.getAbsoluteFile() + "/" + serversArr.get(i))));
            logger.log(Logger.Level.INFO, "Loaded server " + serversArr.get(i) + ".");
        }
    }

    private void loadConfigs(){
        File[] dirListing = configFolder.listFiles();
        JSONParser parser = new JSONParser();

        for(File child : dirListing){
            try {
                JSONObject config = (JSONObject)parser.parse(new FileReader(child));

                if(config.get("name") == null || config.get("launchOptions") == null || config.get("url") == null || config.get("stopCommand") == null || config.get("start") == null){
                    logger.log(Logger.Level.WARN, "Invalid config file '" + child.getPath() + "'. Skipping.");
                    return;
                }

                loadedConfigs.add(new ServerConfig(config));
                logger.log(Logger.Level.INFO, "Loaded server config '" + child.getPath() + "'.");
            }catch (Exception e){
                e.printStackTrace();
                logger.log(Logger.Level.WARN, "Failed to read config '" + child.getPath() + "'. Skipping.");
                return;
            }
        }
    }
}
