package com.simplyalec.servers.manager.util;

public class Logger {
    public enum Level{
        ERR ("ERR"), DEBUG ("DEBUG"), INFO ("INFO"), WARN ("WARN");

        String level;

        Level(String level){
            this.level = level;
        }
    }

    public void log(Level level, String msg){
        //TODO disable debug for prod.
        //if(level != Level.DEBUG)
            System.out.println("[" + level + "] " + msg);
    }

}
