package at.technikum.server.utils.socket;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Socket implements ISocket {

    // Socket
    @Getter
    java.net.Socket socket;

    public Socket(java.net.Socket socket) {
        this.socket = socket;
    }

    // Input Stream
    @Override
    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    // Output Stream
    @Override
    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

}
