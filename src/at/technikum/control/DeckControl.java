package at.technikum.control;

import at.technikum.model.Player;
import at.technikum.repository.DeckRepository;
import at.technikum.repository.DeckRepositoryImpl;
import at.technikum.repository.PlayerRepository;
import at.technikum.repository.PlayerRepositoryImpl;
import at.technikum.serializer.DeckSerializer;
import at.technikum.server.utils.request.RequestImpl;
import at.technikum.server.utils.response.ResponseBuilderImpl;
import at.technikum.server.utils.response.ResponseImpl;
import at.technikum.utils.Printer;
import at.technikum.utils.PrinterImpl;
import at.technikum.utils.tools.TextColor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class DeckControl extends TextColor {
    DeckRepository deckRepository;
    PlayerRepository playerRepository;
    DeckSerializer deckSerializer;
    Printer print;

    public DeckControl(){
        this.print = new PrinterImpl();
        this.playerRepository = new PlayerRepositoryImpl();
        this.deckRepository = new DeckRepositoryImpl();
        this.deckSerializer = new DeckSerializer();
    }

    /**
     * --> LOAD DECK
     **/
    public ResponseImpl get(RequestImpl requestImpl) {
       // System.out.println("# LOAD DECK");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerRepository.getPlayerById(requestImpl.getAuth());

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "USER NOT FOUND" + ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(deckSerializer.message("USER NOT FOUND").toString());
        }
        if (currentPlayer.getDeck() == null) {
            System.out.println(ANSI_RED + "DECK EMPTY" + ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(deckSerializer.message("DECK EMPTY").toString());
        }
        this.print.printDeck(currentPlayer.getDeck());
        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        /** --> JSON OBJECT **/
        JsonObject jsonObject = deckSerializer.convertDeckToJson(currentPlayer.getDeck(),false,true,false);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }




    /** --> LOAD DECK FORMAT **/
    public ResponseImpl getFormat(RequestImpl requestImpl) {
        // System.out.println("# LOAD DECK");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerRepository.getPlayerById(requestImpl.getAuth());

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "USER NOT FOUND" + ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(deckSerializer.message("USER NOT FOUND").toString());
        }
        if (currentPlayer.getDeck() == null) {
            System.out.println(ANSI_RED + "DECK EMPTY" + ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(deckSerializer.message("DECK EMPTY").toString());
        }
        this.print.printDeck(currentPlayer.getDeck());
        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        /** --> JSON OBJECT **/
        JsonObject jsonObject = deckSerializer.convertDeckToJson(currentPlayer.getDeck(),false,false,true);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }



    /**
     * --> SET DECK
     **/
    public ResponseImpl put(RequestImpl requestImpl) {
        //System.out.println("#NEW DECK ");

        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST --> WENN AUTH LEER IST **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> Speichert den BODY in einem String **/
        String jsonString = requestImpl.getBody();
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
        Player currentPlayer = this.playerRepository.getPlayerById(requestImpl.getAuth());
        if (this.deckRepository.setNewDeck(newDeck, currentPlayer.getUserID())) {
            currentPlayer = playerRepository.reloadAccount(currentPlayer);
          //  System.out.println(ANSI_GREEN + "SET NEW DECK!" + ANSI_RESET);
            /** --> JSON OBJECT **/
            JsonObject jsonObject = deckSerializer.convertDeckToJson(currentPlayer.getDeck(),true,false,false);
            /** --> STATUS OK **/
            return new ResponseBuilderImpl().statusOK(jsonObject.toString());
        }

        System.out.println(ANSI_RED + "SET NEW DECK - ERROR" + ANSI_RESET);
        return new ResponseBuilderImpl().statusMethodNotAllowed(deckSerializer.message("SET NEW DECK - ERROR").toString());
    }







}
