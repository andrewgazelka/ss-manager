package com.simplyalec.servers.manager.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.UUID;

public class Config {

    private JSONParser jsonParser;
    private BufferedWriter writer;
    private BufferedReader reader;
    private Path filePath; //The actual Java file
    private Logger logger;
    private JSONObject json; //The parsed JSON from the config.
    //I know, bad naming!

    public Config(Path filePath) {
        this.filePath = filePath;
        logger = new Logger();
        try {
            Files.createDirectories(filePath.getParent());

            jsonParser = new JSONParser();

            if (!Files.exists(filePath)) {
                logger.log(Logger.Level.WARN, "Config not found. Generating one for you!");
                Files.createFile(filePath);
                writeDefaultConfig();
            }

            if(!Files.isReadable(filePath))
            {
                logger.log(Logger.Level.ERR, String.format("Config file {%s} is not readable. Try recreating it", filePath.toAbsolutePath()));
                System.exit(0);
            }

            reader = Files.newBufferedReader(filePath);

            json = (JSONObject) jsonParser.parse(reader);

            if (verifyConfig()) {
                logger.log(Logger.Level.DEBUG, "Loaded config!");
            } else {
                logger.log(Logger.Level.ERR, "Config file can not be verified. Try making sure the file has the correct syntax.");
                System.exit(0);
            }
            writer = Files.newBufferedWriter(filePath);

            writer.close();
        } catch (IOException | ParseException e) {
            logger.log(Logger.Level.ERR, "Failed to read/modify config. Try deleting it and letting SS Manager create a new one.");
            System.exit(0);
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        writer.close();
        reader.close();
    }

    private void writeDefaultConfig() throws IOException {
        JSONObject settings = new JSONObject();
        //Strange warning is strange. Maybe I'm doing something wrong. But it should not cause issues.
        settings.put("token", UUID.randomUUID().toString());
        settings.put("port", 6969);
        logger.log(Logger.Level.INFO, "Check your config to find your auto-generated secret token.");

        Files.write(filePath, Collections.singleton(settings.toJSONString()));

        writer.write(settings.toJSONString());
    }

    private boolean verifyConfig() throws IOException {

        //Check for all required keys
        if (json.get("token") == null
                || json.get("port") == null) {
            return false;
        }
        return true;

    }

    public String getString(String value) {
        return json.get(value).toString();
    }

    public Integer getInt(String value) {
        try {
            return Integer.parseInt(json.get(value).toString());
        } catch (NumberFormatException e) {
            logger.log(Logger.Level.ERR, "Malformed config integer value detected. Defaulting to 0.");
            return 0;
        }
    }

    public Path getConfigFile() {
        return filePath;
    }

    public Logger getLogger() {
        return logger;
    }

    public JSONObject getJson() {
        return json;
    }
}
