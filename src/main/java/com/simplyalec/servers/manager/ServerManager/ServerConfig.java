package com.simplyalec.servers.manager.servermanager;

import org.json.simple.JSONObject;

public class ServerConfig {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLaunchOptions() {
        return launchOptions;
    }

    public void setLaunchOptions(String launchOptions) {
        this.launchOptions = launchOptions;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStopCommand() {
        return stopCommand;
    }

    public void setStopCommand(String stopCommand) {
        this.stopCommand = stopCommand;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    private String name;
    private String launchOptions;
    private String url;
    private String stopCommand;
    private String start;

    public ServerConfig(String name, String launchOptions, String url, String stopCommand, String start){
        this.name = name;
        this.launchOptions = launchOptions;
        this.url = url;
        this.stopCommand = stopCommand;
        this.start = start;
    }

    public ServerConfig(JSONObject config){
        this.name = config.get("name").toString();
        this.launchOptions = config.get("launchOptions").toString();
        this.url = config.get("url").toString();
        this.stopCommand = config.get("stopCommand").toString();
        this.start = config.get("start").toString();
    }
}
