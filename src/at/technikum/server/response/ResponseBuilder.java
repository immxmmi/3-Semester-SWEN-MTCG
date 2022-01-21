package at.technikum.server.response;

import at.technikum.server.request.RequestImpl;

public interface ResponseBuilder {

    // STATUS OK
    ResponseImpl statusOK(String body);

    // BAD REQUEST
    ResponseImpl statusBAD(String body);

    // NICHT AUTH
    ResponseImpl statusUnAuthorized(String body);

    // Wenn der Request nicht verfügbar ist
    ResponseImpl statusNotFound(String body);

    // WENN METHODE NICHT FUNKTIONIERT
    ResponseImpl statusMethodNotAllowed(String body);

    // PUT REQUEST
    ResponseImpl statusCreated(String body);

    // NICHT ERLAUBT
    ResponseImpl statusForbidden(String body);

    ResponseImpl requestErrorHandler(RequestImpl requestImpl, boolean auth, boolean body, boolean player);
}
