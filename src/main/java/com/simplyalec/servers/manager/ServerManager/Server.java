package com.simplyalec.servers.manager.ServerManager;

import org.json.simple.JSONObject;

import java.io.File;

public class Server {
    File rootDir;
    JSONObject config;
    String id;
    String name;

    public Server(File rootDir){
        this.rootDir = rootDir;

    }

    public void executeCommand(){

    }
    public void start(){

    }
    public void forceKill(){

    }
    public void niceKill(){

    }
    public String getRootDir(){
        return "";
    }


}
