package at.technikum.server.utils.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface ISocket {
    Socket getSocket();

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;
}
