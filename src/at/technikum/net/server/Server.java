package at.technikum.net.server;

import java.net.ServerSocket;

public interface Server {
    void listen();

    void closeServer();

    ServerSocket getListener();
}
