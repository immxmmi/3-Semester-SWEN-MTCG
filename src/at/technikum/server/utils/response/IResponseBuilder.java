package at.technikum.server.utils.response;

import at.technikum.server.utils.request.Request;

import java.io.BufferedWriter;

public interface IResponseBuilder {

    void write(BufferedWriter writer, IResponse response);

    // STATUS OK
    Response statusOK(String body);

    // BAD REQUEST
    Response statusBAD(String body);

    // NICHT AUTH
    Response statusUnAuthorized(String body);

    // Wenn der Request nicht verfügbar ist
    Response statusNotFound(String body);

    // WENN METHODE NICHT FUNKTIONIERT
    Response statusMethodNotAllowed(String body);

    // PUT REQUEST
    Response statusCreated(String body);

    // NICHT ERLAUBT
    Response statusForbidden(String body);

    Response requestErrorHandler(Request request, boolean auth, boolean body);
}
