package at.technikum.control;

import at.technikum.model.Player;
import at.technikum.model.Stack;
import at.technikum.model.StackImpl;
import at.technikum.repository.PlayerRepository;
import at.technikum.repository.StackRepository;
import at.technikum.repository.PlayerRepositoryImpl;
import at.technikum.repository.StackRepositoryImpl;
import at.technikum.server.utils.request.Request;
import at.technikum.server.utils.response.Response;
import at.technikum.server.utils.response.ResponseBuilder;
import at.technikum.utils.IPrinter;
import at.technikum.utils.Printer;
import at.technikum.utils.tools.TextColor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StackControl extends TextColor {
    StackRepository stackRepository;
    PlayerRepository playerRepository;
    IPrinter print;
    Gson gson;

    public StackControl(){
        this.print = new Printer();
        this.playerRepository = new PlayerRepositoryImpl();
        this.gson = new Gson();
        this.stackRepository = new StackRepositoryImpl();
    }

    /**
     * --> LOAD STACK
     **/
    public Response get(Request request) {
        System.out.println("# STACK ");
        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new ResponseBuilder().statusBAD(this.message("BAD REQUEST").toString());
        }
        /** --> Wenn AUTH Leer ist **/
        if (request.getAuth() == null) {
            System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
            new ResponseBuilder().statusUnAuthorized(this.message("NO TOKEN").toString());
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerRepository.getPlayerById(request.getAuth());


        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed(this.message("User NOT FOUND").toString());
        }

        this.print.printStack(currentPlayer.getStack());
        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);

        /** --> JSON OBJECT **/
        JsonObject jsonObject = convertStackToJson(currentPlayer.getStack(),true,false);
        /** --> STATUS OK **/
        return new ResponseBuilder().statusOK(jsonObject.toString());
    } // TODO: 10.01.2022 Fertig

    /** Wandelt Java Package-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    private JsonObject convertStackToJson(Stack currentStack, boolean userID, boolean stack){
        JsonObject object = new JsonParser().parse(gson.toJson(currentStack, StackImpl.class)).getAsJsonObject();
        if(!userID){object.remove("userID");}
        if(!stack){object.remove("stack");}
        return object;
    } // TODO: 10.01.2022 Fertig

    /** --> wandelt message String in JSON-Objekt um **/
    private JsonObject message(String message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Message",message);
        return jsonObject;
    } // TODO: 10.01.2022 Fertig
}
