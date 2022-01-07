package at.technikum.server;

import java.net.ServerSocket;

public interface IServer {
    void listen();

    void closeServer();

    ServerSocket getListener();
}
