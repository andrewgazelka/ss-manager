package com.simplyalec.servers.manager.SocketCommands;

import com.simplyalec.servers.manager.ServerManager.Server;

public class StartCommand implements SocketCommand {
    @Override
    public void onTrigger(Server server) {

    }

    @Override
    public String trigger() {
        return "startServer";
    }
}
