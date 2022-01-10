package at.technikum.control;

import at.technikum.model.DeckImpl;
import at.technikum.model.IDeck;
import at.technikum.model.Player;
import at.technikum.repository.*;
import at.technikum.server.utils.request.Request;
import at.technikum.server.utils.response.Response;
import at.technikum.server.utils.response.ResponseBuilder;
import at.technikum.utils.IPrinter;
import at.technikum.utils.Printer;
import at.technikum.utils.tools.TextColor;
import com.google.gson.*;

import java.util.ArrayList;

public class DeckControl extends TextColor {
    DeckRepository deckRepository;
    PlayerRepository playerRepository;
    IPrinter print;
    Gson gson;

    public DeckControl(){
        this.print = new Printer();
        this.playerRepository = new PlayerRepositoryImpl();
        this.gson = new Gson();
        this.deckRepository = new DeckRepositoryImpl();
    }

    /**
     * --> LOAD DECK
     **/
    public Response deck(Request request) {
        System.out.println("# LOAD DECK");

        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST **/
        Response response = new ResponseBuilder().requestErrorHandler(request, true, false);
        if (response != null) {
            return response;
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerRepository.getPlayerById(request.getAuth());

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "USER NOT FOUND" + ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed(this.Message("USER NOT FOUND").toString());
        }
        if (currentPlayer.getDeck() == null) {
            System.out.println(ANSI_RED + "DECK EMPTY" + ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed(this.Message("DECK EMPTY").toString());
        }
        this.print.printDeck(currentPlayer.getDeck());
        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        /** --> JSON OBJECT **/
        JsonObject jsonObject = convertDeckToJson(currentPlayer.getDeck(),true,true,true);
        /** --> STATUS OK **/
        return new ResponseBuilder().statusOK(jsonObject.toString());
    }


    /**
     * --> SET DECK
     **/
    public Response setNewDeck(Request request) {
        System.out.println("#NEW DECK ");

        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST --> WENN AUTH LEER IST **/
        Response response = new ResponseBuilder().requestErrorHandler(request, true, true);
        if (response != null) {
            return response;
        }

        /** --> Speichert den BODY in einem String **/
        String jsonString = request.getBody();
        /** --> wandelt den String in JSON-Element um **/
        JsonElement cardsElement = new JsonParser().parse(jsonString);
        /** --> wandelt das JSON-Element in ein JSON-Array **/
        JsonArray cardList = cardsElement.getAsJsonArray();
        /** --> ARRAYLIST mit allen deck Karten **/
        ArrayList<String> newDeck = new ArrayList();

        for (int i = 0; i < cardList.size(); i++) {
            newDeck.add("C-" + cardList.get(i).getAsString());
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerRepository.getPlayerById(request.getAuth());
        if (this.deckRepository.setNewDeck(newDeck, currentPlayer.getUserID())) {
            currentPlayer = playerRepository.reloadAccount(currentPlayer);
            System.out.println(ANSI_GREEN + "SET NEW DECK!" + ANSI_RESET);
            /** --> JSON OBJECT **/
            JsonObject jsonObject = convertDeckToJson(currentPlayer.getDeck(),true,true,true);
            /** --> STATUS OK **/
            return new ResponseBuilder().statusOK(jsonObject.toString());
        }

        System.out.println(ANSI_RED + "SET NEW DECK - ERROR" + ANSI_RESET);
        return new ResponseBuilder().statusMethodNotAllowed(this.Message("SET NEW DECK - ERROR").toString());
    }



    /** Wandelt Java Package-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    private JsonObject convertDeckToJson(IDeck currentDeck, boolean userID, boolean deck, boolean cardIDList){
        JsonObject object = new JsonParser().parse(gson.toJson(currentDeck, DeckImpl.class)).getAsJsonObject();
        if(!userID){object.remove("userID");}
        if(!deck){object.remove("deck");}
        if(!cardIDList){object.remove("cardIDList");}
        return object;
    } // TODO: 10.01.2022 Fertig

    /** --> wandelt message String in JSON-Objekt um **/
    private JsonObject Message(String message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Message",message);
        return jsonObject;
    } // TODO: 10.01.2022 Fertig


}
