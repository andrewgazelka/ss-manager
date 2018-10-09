package com.simplyalec.servers.manager.socketcommands;

import com.simplyalec.servers.manager.servermanager.Server;

public class StartCommand implements SocketCommand {
    @Override
    public void onTrigger(Server server) {

    }

    @Override
    public String trigger() {
        return "startServer";
    }
}
