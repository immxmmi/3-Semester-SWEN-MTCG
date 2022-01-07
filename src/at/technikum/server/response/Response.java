package at.technikum.server.response;


import at.technikum.server.request.Request;
import at.technikum.tools.Tools;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
public class Response extends Tools implements IResponse {
    String delimiter = " ";
    String end = "\r\n";

    @Getter
    @Builder.Default
    String version = "HTTP/1.1";
    @Getter
    @Builder.Default
    int status = 200;
    @Getter
    @Builder.Default
    String reasonPhrase = "OK";

    @Getter
    @Builder.Default
    HashMap<String, String> header = new HashMap<>();
    @Getter
    @Builder.Default
    String body = "";


    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/

    public Response() {
        this.header = new HashMap<>();
        this.version = "HTTP/1.1";
        this.status = 200;
        this.reasonPhrase = "OK";
    }

    /*******************************************************************/

    @Override
    public void write(BufferedWriter writer) {

        if (!this.header.containsKey("Host")) {
            try {
                this.header.put("Host", InetAddress.getLocalHost().getHostName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            StringBuilder currentBuffer = new StringBuilder();
            currentBuffer.append(status);   // Status
            currentBuffer.append(delimiter); // Trennzeichen
            currentBuffer.append(reasonPhrase); // OK
            currentBuffer.append(delimiter); // Trennzeichen
            currentBuffer.append(end); // END

            // HEADER wird zum Buffer hinzugefügt
            for (Map.Entry<String, String> entry : this.header.entrySet()) {
                currentBuffer.append(entry.getKey());   // KEY wird zum Buffer hinzugefügt
                currentBuffer.append(": ");             // Trennzeichen
                currentBuffer.append(entry.getValue()); // VALUE wird zum Buffer hinzugefügt
                currentBuffer.append(end); // END
            }

            //END HEADER
            currentBuffer.append(end);
            //BODY
            if (this.body != null && this.body.length() > 0) {
                currentBuffer.append(end);
            }
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
    public Response statusOK() {
        return Response.builder()
                .status(200)
                .reasonPhrase("OK")
                .body("OK")
                .build();
    }

    // BAD REQUEST
    @Override
    public Response statusBAD() {
        return Response.builder()
                .status(400)
                .reasonPhrase("BAD REQUEST")
                .body("BAD REQUEST")
                .build();
    }

    // NICHT AUTH
    @Override
    public Response statusUnAuthorized() {
        return Response.builder()
                .status(401)
                .reasonPhrase("Unauthorized")
                .body("Unauthorized")
                .build();
    }

    // Wenn der Request nicht verfügbar ist
    @Override
    public Response statusNotFound() {
        return Response.builder()
                .status(404)
                .reasonPhrase("NOT FOUND")
                .body("Server kann angeforderte Ressource nicht finden. Dieser Antwort-Code ist wahrscheinlich der bekannteste aufgrund seiner Häufigkeit, mit der er im Web auftritt.")
                .build();
    }

    // WENN METHODE NICHT FUNKTIONIERT
    @Override
    public Response statusMethodNotAllowed() {
        return Response.builder()
                .status(405)
                .reasonPhrase("METHOD NOT ALLOWED")
                .body("METHOD -- ERROR")
                .build();
    }

    // PUT REQUEST
    @Override
    public Response statusCreated() {
        return Response.builder()
                .status(201)
                .reasonPhrase("Created")
                .body("PUT -- ")
                .build();
    }

    // NICHT ERLAUBT
    @Override
    public Response statusForbidden() {
        return Response.builder()
                .status(403)
                .reasonPhrase("Forbidden")
                .body("Der Client hat keine Zugriffsrechte auf den Inhalt, daher verweigert der Server eine ordnungsgemäße Antwort.")
                .build();
    }

    /*******************************************************************/

    @Override
    public Response requestErrorHandler(Request request, boolean auth, boolean body) {
        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new Response().statusBAD();
        }
        /** --> Wenn AUTH Leer ist **/
        if (auth) {
            if (request.getAuth() == null) {
                System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
                return new Response().statusUnAuthorized();
            }
        }
        /** --> WENN BODY LEER IST **/
        if (body) {
            if (request.getBody().equals("")) {
                System.out.println(ANSI_RED + "BODY EMPTY" + ANSI_RESET);
                return new Response().statusBAD();
            }
        }
        return null;
    }

}
