package at.technikum.server.response;

import at.technikum.logger.LoggerStatic;
import at.technikum.model.repository.Player;
import at.technikum.server.request.RequestImpl;
import at.technikum.handler.repository.PlayerHandler;
import at.technikum.handler.PlayerHandlerImpl;
import com.google.gson.JsonObject;

public class ResponseBuilderImpl implements ResponseBuilder {

    private LoggerStatic loggerStatic;

    public ResponseBuilderImpl() {
        this.loggerStatic = LoggerStatic.getInstance();
    }


    /*******************************************************************/
    /**                            MESSAGE                            **/
    /*******************************************************************/

    // STATUS OK
    @Override
    public ResponseImpl statusOK(String body) {
        return ResponseImpl.builder()
                .status(200)
                .reasonPhrase("OK")
                .body(body)
                .build();
    }

    // BAD REQUEST
    @Override
    public ResponseImpl statusBAD(String body) {
        return ResponseImpl.builder()
                .status(400)
                .reasonPhrase(body)
                .body(body)
                .build();
    }

    // NICHT AUTH
    @Override
    public ResponseImpl statusUnAuthorized(String body) {
        return ResponseImpl.builder()
                .status(401)
                .reasonPhrase(body)
                .body(body)
                .build();
    }

    // Wenn der Request nicht verfügbar ist
    @Override
    public ResponseImpl statusNotFound(String body) {
        return ResponseImpl.builder()
                .status(404)
                .reasonPhrase(body)
                .body(body)
                .build();
    }

    // WENN METHODE NICHT FUNKTIONIERT
    @Override
    public ResponseImpl statusMethodNotAllowed(String body) {
        return ResponseImpl.builder()
                .status(405)
                .reasonPhrase(body)
                .body(body)
                .build();
    }

    // PUT REQUEST
    @Override
    public ResponseImpl statusCreated(String body) {
        return ResponseImpl.builder()
                .status(201)
                .reasonPhrase(body)
                .body(body)
                .build();
    }

    // NICHT ERLAUBT
    @Override
    public ResponseImpl statusForbidden(String body) {
        return ResponseImpl.builder()
                .status(403)
                .reasonPhrase(body)
                .body(body)
                .build();
    }

    /*******************************************************************/



    @Override
    public ResponseImpl requestErrorHandler(RequestImpl requestImpl, boolean auth, boolean body, boolean player) {
        /** --> Wenn REQUEST LEER IST **/
        if (requestImpl == null) {
            //System.out.println(TextColor.ANSI_RED + "BAD REQUEST" + TextColor.ANSI_RESET);
            loggerStatic.log("\nBAD REQUEST\n");
            return new ResponseBuilderImpl().statusBAD(this.Message("BAD REQUEST").toString());
        }
        /** --> Wenn AUTH LEER IST **/
        if (auth) {
            if (requestImpl.getAuth() == null) {
                //System.out.println(TextColor.ANSI_RED + "NO TOKEN" + TextColor.ANSI_RESET);
                loggerStatic.log("\nNO TOKEN\n");
                return new ResponseBuilderImpl().statusUnAuthorized(this.Message("NO TOKEN").toString());
            }
        }
        /** --> WENN BODY LEER IST **/
        if (body) {
            if (requestImpl.getBody().equals("")) {
                //System.out.println(TextColor.ANSI_RED + "BODY EMPTY" + TextColor.ANSI_RESET);
                loggerStatic.log("\nBODY EMPTY\n");
                return new ResponseBuilderImpl().statusBAD(this.Message("BODY EMPTY").toString());
            }
        }
        /** --> WENN USER NICHT EXISTIERT **/
        if(player){
            /** --> INSTANCE **/
            PlayerHandler playerHandler = new PlayerHandlerImpl();
            Player currentPlayer = playerHandler.getItemById(requestImpl.getAuth());
            /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
            if (currentPlayer == null) {
                //System.out.println(TextColor.ANSI_RED + "USER NOT FOUND" + TextColor.ANSI_RESET);
                loggerStatic.log("\nUSER NOT FOUND\n");
                return new ResponseBuilderImpl().statusMethodNotAllowed("USER NOT FOUND");
            }


            return null;
        }
        return null;
    }

    private JsonObject Message(String message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Message",message);
        return jsonObject;
    }
}
