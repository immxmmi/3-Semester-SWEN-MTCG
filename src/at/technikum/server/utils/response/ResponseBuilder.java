package at.technikum.server.utils.response;

import at.technikum.server.utils.request.Request;
import at.technikum.utils.tools.TextColor;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;

public class ResponseBuilder implements IResponseBuilder{
    @Override
    public void write(BufferedWriter writer, IResponse response) {

        try {
            if(response.getBody() == null){
                response.setBody("");
            }
            StringBuilder currentBuffer = new StringBuilder();
            currentBuffer.append(response.getVersion() +" "+ response.getStatus()+" "+ response.getReasonPhrase() + "\r\n");   // Status
            currentBuffer.append("Content-Type: "+response.getContentTyp()+"\r\n");
            currentBuffer.append("Content-Length: "+response.getBody().length()+"\r\n");
            currentBuffer.append("\r\n"+response.getBody());

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
                .reasonPhrase(body)
                .body(body)
                .build();
    }

    // NICHT AUTH
    @Override
    public Response statusUnAuthorized(String body) {
        return Response.builder()
                .status(401)
                .reasonPhrase(body)
                .body(body)
                .build();
    }

    // Wenn der Request nicht verfügbar ist
    @Override
    public Response statusNotFound(String body) {
        return Response.builder()
                .status(404)
                .reasonPhrase(body)
                .body(body)
                .build();
    }

    // WENN METHODE NICHT FUNKTIONIERT
    @Override
    public Response statusMethodNotAllowed(String body) {
        return Response.builder()
                .status(405)
                .reasonPhrase(body)
                .body(body)
                .build();
    }

    // PUT REQUEST
    @Override
    public Response statusCreated(String body) {
        return Response.builder()
                .status(201)
                .reasonPhrase(body)
                .body(body)
                .build();
    }

    // NICHT ERLAUBT
    @Override
    public Response statusForbidden(String body) {
        return Response.builder()
                .status(403)
                .reasonPhrase(body)
                .body(body)
                .build();
    }

    /*******************************************************************/

    @Override
    public Response requestErrorHandler(Request request, boolean auth, boolean body) {
        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(TextColor.ANSI_RED + "BAD REQUEST" + TextColor.ANSI_RESET);

            return new ResponseBuilder().statusBAD(this.Message("BAD REQUEST").toString());
        }
        /** --> Wenn AUTH Leer ist **/
        if (auth) {
            if (request.getAuth() == null) {
                System.out.println(TextColor.ANSI_RED + "NO TOKEN" + TextColor.ANSI_RESET);
                return new ResponseBuilder().statusUnAuthorized(this.Message("NO TOKEN").toString());
            }
        }
        /** --> WENN BODY LEER IST **/
        if (body) {
            if (request.getBody().equals("")) {
                System.out.println(TextColor.ANSI_RED + "BODY EMPTY" + TextColor.ANSI_RESET);
                return new ResponseBuilder().statusBAD(this.Message("BODY EMPTY").toString());
            }
        }
        return null;
    }

    private JsonObject Message(String message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Message",message);
        return jsonObject;
    }
}
