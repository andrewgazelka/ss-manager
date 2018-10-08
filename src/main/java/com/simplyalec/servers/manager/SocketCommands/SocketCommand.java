package com.simplyalec.servers.manager.SocketCommands;

import com.simplyalec.servers.manager.ServerManager.Server;

public interface SocketCommand {
    void onTrigger(Server server);
    String trigger();
}
