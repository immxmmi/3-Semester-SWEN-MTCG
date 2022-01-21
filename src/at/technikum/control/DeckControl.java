package at.technikum.control;

import at.technikum.control.repository.Get;
import at.technikum.control.repository.Put;
import at.technikum.model.repository.Player;
import at.technikum.repository.DeckRepository;
import at.technikum.repository.DeckRepositoryImpl;
import at.technikum.repository.PlayerRepository;
import at.technikum.repository.PlayerRepositoryImpl;
import at.technikum.serializer.DeckSerializer;
import at.technikum.net.server.utils.request.RequestImpl;
import at.technikum.net.server.utils.response.ResponseBuilderImpl;
import at.technikum.net.server.utils.response.ResponseImpl;
import at.technikum.utils.Printer;
import at.technikum.utils.PrinterImpl;
import at.technikum.utils.tools.TextColor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class DeckControl implements Get, Put {

    Printer print;
    TextColor textColor;
    DeckSerializer deckSerializer;
    DeckRepository deckRepository;
    PlayerRepository playerRepository;

    public DeckControl(){
        this.print = new PrinterImpl();
        this.textColor = new TextColor();
        this.playerRepository = new PlayerRepositoryImpl();
        this.deckRepository = new DeckRepositoryImpl();
        this.deckSerializer = new DeckSerializer();
    }

    /** --> LOAD DECK **/
    @Override
    public ResponseImpl get(RequestImpl requestImpl) {
       // System.out.println("# LOAD DECK");
        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }
        /** --> INSTANCE **/
        Player currentPlayer = this.playerRepository.getPlayerById(requestImpl.getAuth());

        if (currentPlayer.getDeck() == null) {
            System.out.println(textColor.ANSI_RED + "DECK EMPTY" + textColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(deckSerializer.message("DECK EMPTY").toString());
        }
        this.print.printDeck(currentPlayer.getDeck());
        System.out.println(textColor.ANSI_GREEN + "LOADING FINISHED!" + textColor.ANSI_RESET);
        /** --> JSON OBJECT **/
        JsonObject jsonObject = deckSerializer.convertDeckToJson(currentPlayer.getDeck(),false,true,false);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }

    /** --> LOAD DECK FORMAT **/
    public ResponseImpl format(RequestImpl requestImpl) {
        // System.out.println("# LOAD DECK");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerRepository.getPlayerById(requestImpl.getAuth());

        if (currentPlayer.getDeck() == null) {
            System.out.println(textColor.ANSI_RED + "DECK EMPTY" + textColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(deckSerializer.message("DECK EMPTY").toString());
        }

        this.print.printDeck(currentPlayer.getDeck());
        System.out.println(textColor.ANSI_GREEN + "LOADING FINISHED!" + textColor.ANSI_RESET);
        /** --> JSON OBJECT **/
        JsonObject jsonObject = deckSerializer.convertDeckToJson(currentPlayer.getDeck(),false,false,true);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }

    /*** --> SET DECK **/
    @Override
    public ResponseImpl put(RequestImpl requestImpl) {
        //System.out.println("#NEW DECK ");

        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, true, true);
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

        System.out.println(textColor.ANSI_RED + "SET NEW DECK - ERROR" + textColor.ANSI_RESET);
        return new ResponseBuilderImpl().statusMethodNotAllowed(deckSerializer.message("SET NEW DECK - ERROR").toString());
    }

}
