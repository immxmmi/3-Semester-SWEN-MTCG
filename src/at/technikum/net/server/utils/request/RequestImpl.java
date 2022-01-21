package at.technikum.net.server.utils.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

// https://www.ionos.at/digitalguide/hosting/hosting-technik/http-request-erklaert/
// - TEST HEADER

// curl -X GET http://localhost:10001/users/kienboec --header "Authorization: Basic kienboec-mtcgToken"
@Builder (toBuilder = true)
public class RequestImpl implements Request {
    @Setter
    @Getter
    @Builder.Default
    private String host = "localhost";
    @Setter
    @Getter
    private String version;
    @Setter
    @Getter
    private String method;
    @Setter
    @Getter
    private String path;
    @Setter
    @Getter
    private String body;
    @Setter
    @Getter
    private String auth;
    @Setter
    @Getter
    @Builder.Default
    private HashMap<String, String> params = new HashMap<>();
    @Setter
    @Getter
    @Builder.Default
    private HashMap<String, String> headers = new HashMap<>();
}