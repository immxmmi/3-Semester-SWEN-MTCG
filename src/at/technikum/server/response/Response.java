package at.technikum.server.response;

import at.technikum.server.request.Request;
import at.technikum.utils.tools.Tools;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.BufferedWriter;
import java.io.IOException;

@Builder
@AllArgsConstructor
public class Response extends Tools implements IResponse{
    private static final String delimiter = "\n";
    private static final String end = "\r\n";

    @Getter
    @Builder.Default
    String version = "HTTP/1.1";
    @Getter
    @Builder.Default
    String server = "localhost";
    @Getter
    @Builder.Default
    String contentTyp = "text/plain; charset=utf-8";
    @Getter
    @Builder.Default
    String content = "<!DOCTYPE html><html><body><h1>Hello, World!</h1></body></html>";
    @Getter
    @Builder.Default
    int contentLength = 0;

    @Getter
    @Builder.Default
    int status = 200;
    @Getter
    @Builder.Default
    String reasonPhrase = "OK";

    @Getter
    @Builder.Default
    String body = "";


    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/

    public Response() {
        this.version = "HTTP/1.1";
        this.status = 200;
        this.reasonPhrase = "OK";
    }

    /*******************************************************************/

    @Override
    public void write(BufferedWriter writer) {

        try {
            if(this.body == null){
                this.body = "";
            }
            StringBuilder currentBuffer = new StringBuilder();
            currentBuffer.append(version +" "+ status+" "+ reasonPhrase + "\r\n");   // Status
            currentBuffer.append("Content-Type: "+contentTyp+"\r\n");
            currentBuffer.append("Content-Length: "+body.length()+"\r\n");
            currentBuffer.append("\r\n"+body);
            //currentBuffer.append(end); // END

            System.out.println(currentBuffer);
            writer.write(currentBuffer.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /*******************************************************************/
    /**                            MESSAGE                            **/
    /*******************************************************************/



    // STATUS OK
    @Override
    public Response statusOK(String body) {
        return Response.builder()
                .status(200)
                .reasonPhrase("OK")
                .body(body)
                .build();
    }

    // BAD REQUEST
    @Override
    public Response statusBAD(String body) {
        return Response.builder()
                .status(400)
                .reasonPhrase("BAD REQUEST")
                .body(body)
                .build();
    }

    // NICHT AUTH
    @Override
    public Response statusUnAuthorized(String body) {
        return Response.builder()
                .status(401)
                .reasonPhrase("Unauthorized")
                .body(body)
                .build();
    }

    // Wenn der Request nicht verfügbar ist
    @Override
    public Response statusNotFound(String body) {
        return Response.builder()
                .status(404)
                .reasonPhrase("NOT FOUND")
                .body(body)
                .build();
    }

    // WENN METHODE NICHT FUNKTIONIERT
    @Override
    public Response statusMethodNotAllowed(String body) {
        return Response.builder()
                .status(405)
                .reasonPhrase("METHOD NOT ALLOWED")
                .body(body)
                .build();
    }

    // PUT REQUEST
    @Override
    public Response statusCreated(String body) {
        return Response.builder()
                .status(201)
                .reasonPhrase("Created")
                .body(body)
                .build();
    }

    // NICHT ERLAUBT
    @Override
    public Response statusForbidden(String body) {
        return Response.builder()
                .status(403)
                .reasonPhrase("Forbidden")
                .body(body)
                .build();
    }

    /*******************************************************************/

    @Override
    public Response requestErrorHandler(Request request, boolean auth, boolean body) {
        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new Response().statusBAD("BAD REQUEST");
        }
        /** --> Wenn AUTH Leer ist **/
        if (auth) {
            if (request.getAuth() == null) {
                System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
                return new Response().statusUnAuthorized("NO TOKEN");
            }
        }
        /** --> WENN BODY LEER IST **/
        if (body) {
            if (request.getBody().equals("")) {
                System.out.println(ANSI_RED + "BODY EMPTY" + ANSI_RESET);
                return new Response().statusBAD("BODY EMPTY");
            }
        }
        return null;
    }

}
