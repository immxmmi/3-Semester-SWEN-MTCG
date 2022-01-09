package at.technikum.server.response;

import at.technikum.server.request.Request;

import java.io.BufferedWriter;

// https://stackoverflow.com/questions/19402482/how-can-i-get-the-full-reason-phrase-or-the-raw-response-with-jax-rs
public interface IResponse {
    // Wenn der Request nicht verfügbar ist
    Response statusNotFound(String body);


    // Wenn Status OK ist
    Response statusOK(String body);

    // BAD REQUEST
    Response statusBAD(String body);

    void write(BufferedWriter writer);

    String getVersion();

    int getStatus();

    String getReasonPhrase();


    String getBody();

    // BAD REQUEST
    Response statusUnAuthorized(String body);

    Response statusMethodNotAllowed(String body);

    // PUT REQUEST
    Response statusCreated(String body);

    Response statusForbidden(String body);

    Response requestErrorHandler(Request request, boolean auth, boolean body);
}
