package com.simplyalec.servers.manager.servermanager;

import org.json.simple.JSONObject;

import java.io.File;

public class Server {

    private File rootDir;
    private JSONObject config;
    private String id;
    private String name;

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
