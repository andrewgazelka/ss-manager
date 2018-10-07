package com.simplyalec.servers.manager.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.UUID;

public class Config {

    private File config;
    private Logger logger;

    public Config(String filePath) {
        logger = new Logger();

        try {
            File f = new File(filePath);
            this.config = f;
            if (f.isFile() && f.canRead() && verifyConfig()) {
                logger.log(Logger.Level.DEBUG, "Loaded config!");
            } else {
                logger.log(Logger.Level.WARN, "Config not found. Generating one for you!");
                writeDefaultConfig();

            }
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.log(Logger.Level.ERR, "Failed to read/modify config.");
            System.exit(0);
        }
    }

    private void writeDefaultConfig() throws IOException{
        JSONObject settings = new JSONObject();
        settings.put("token", UUID.randomUUID().toString()); //TODO is this ok? It should be!
        logger.log(Logger.Level.INFO, "Check your config to find your auto-generated secret token.");

        FileWriter file = new FileWriter(config);
        file.write(settings.toJSONString());
        file.close();
    }

    private boolean verifyConfig() throws IOException{
        JSONParser parser = new JSONParser();
        try {
            Object test = parser.parse(new FileReader(config));
        }catch (ParseException e){
            return false;
        }
        return true;

    }

    public String getValue(String value){
        return "asd";
    }

}
