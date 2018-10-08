package com.simplyalec.servers.manager.util;

public class SocketObject {

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    private String server;
    private String command;


    public SocketObject(String server, String command) {
        this.server = server;
        this.command = command;
    }
}