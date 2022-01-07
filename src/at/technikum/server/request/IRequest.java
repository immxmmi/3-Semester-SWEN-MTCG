package at.technikum.server.request;

import java.io.BufferedReader;
import java.util.Map;

public interface IRequest {

    void read(BufferedReader reader);

    String getHost();

    String getVersion();

    String getMethod();

    String getPath();

    String getBody();

    String getAuth();

    Map<String, String> getParams();

    Map<String, String> getHeaders();
}
