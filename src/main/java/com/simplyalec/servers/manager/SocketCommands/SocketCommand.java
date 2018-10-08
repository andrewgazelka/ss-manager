package com.simplyalec.servers.manager.socketcommands;

import com.simplyalec.servers.manager.servermanager.Server;

public interface SocketCommand {
    void onTrigger(Server server);
    String trigger();
}
