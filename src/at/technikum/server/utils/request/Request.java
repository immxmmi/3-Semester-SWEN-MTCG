package at.technikum.server.utils.request;

import java.util.HashMap;
import java.util.Map;

public interface Request {

    String getHost();

    String getVersion();

    String getMethod();

    String getPath();

    String getBody();

    String getAuth();

    Map<String, String> getParams();

    Map<String, String> getHeaders();

    void setHost(String host);

    void setVersion(String version);

    void setMethod(String method);

    void setPath(String path);

    void setBody(String body);

    void setAuth(String auth);

    void setParams(HashMap<String, String> params);

    void setHeaders(HashMap<String, String> headers);
}
