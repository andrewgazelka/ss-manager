package com.simplyalec.servers.manager.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.UUID;

public class Config {

    private File configFile; //The actual Java file
    private Logger logger;
    private JSONObject config; //The parsed JSON from the config.
    //I know, bad naming!

    public Config(String filePath) {
        logger = new Logger();

        try {
            File f = new File(filePath);
            this.configFile = f;
            if (f.isFile() && f.canRead() && verifyConfig()) {
                logger.log(Logger.Level.DEBUG, "Loaded config!");
            } else {
                logger.log(Logger.Level.WARN, "Config not found. Generating one for you!");
                writeDefaultConfig();
            }
            JSONParser parser = new JSONParser();
            config = (JSONObject)parser.parse(new FileReader(configFile));
        } catch (Exception ex) { //Just catch them all.
            ex.printStackTrace();
            logger.log(Logger.Level.ERR, "Failed to read/modify config. Try deleting it and letting SS Manager create a new one.");
            System.exit(0);
        }
    }

    private void writeDefaultConfig() throws IOException{
        JSONObject settings = new JSONObject();
        //Strange warning is strange. Maybe I'm doing something wrong. But it should not cause issues.
        settings.put("token", UUID.randomUUID().toString());
        settings.put("port", 6969);
        logger.log(Logger.Level.INFO, "Check your config to find your auto-generated secret token.");

        FileWriter file = new FileWriter(configFile);
        file.write(settings.toJSONString());
        file.close();
    }

    private boolean verifyConfig() throws IOException{
        JSONParser parser = new JSONParser();
        try {
            JSONObject test = (JSONObject)parser.parse(new FileReader(configFile));
            //Check for all required keys
            if(test.get("token") == null
                    || test.get("port") == null){
                return false;
            }
        }catch (ParseException e){
            return false;
        }
        return true;

    }

    public String getString(String value){
        return config.get(value).toString();
    }
    public Integer getInt(String value){
        try {
            return Integer.parseInt(config.get(value).toString());
        }catch (NumberFormatException e){
            logger.log(Logger.Level.ERR, "Malformed config integer value detected. Defaulting to 0.");
            return 0;
        }
    }

}
