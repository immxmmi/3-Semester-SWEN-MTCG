package at.technikum.server.response;
import at.technikum.server.request.Request;

import java.io.BufferedWriter;
import java.util.Map;

// https://stackoverflow.com/questions/19402482/how-can-i-get-the-full-reason-phrase-or-the-raw-response-with-jax-rs
public interface IResponse {
    // Wenn der Request nicht verfügbar ist
    Response statusNotFound();

    // Wenn Status OK ist
    Response statusOK();

    // BAD REQUEST
    Response statusBAD();

    void write(BufferedWriter writer);

    String getVersion();

    int getStatus();

    String getReasonPhrase();

    Map<String, String> getHeader();

    String getBody();

    // BAD REQUEST
    Response statusUnAuthorized();

    Response statusMethodNotAllowed();

    // PUT REQUEST
    Response statusCreated();

    Response statusForbidden();

    Response requestErrorHandler(Request request, boolean auth, boolean body);
}
