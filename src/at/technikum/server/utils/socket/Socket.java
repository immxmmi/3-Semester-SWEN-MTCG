package at.technikum.server.utils.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Socket {
    java.net.Socket getSocket();

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;
}
