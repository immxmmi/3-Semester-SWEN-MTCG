package at.technikum.control;

import at.technikum.control.repository.Get;
import at.technikum.control.repository.Put;
import at.technikum.handler.DeckHandlerImpl;
import at.technikum.handler.PlayerHandlerImpl;
import at.technikum.handler.repository.DeckHandler;
import at.technikum.handler.repository.PlayerHandler;
import at.technikum.logger.LoggerStatic;
import at.technikum.model.repository.Player;
import at.technikum.serializer.DeckSerializer;
import at.technikum.server.request.RequestImpl;
import at.technikum.server.response.ResponseBuilderImpl;
import at.technikum.server.response.ResponseImpl;
import at.technikum.utils.Printer;
import at.technikum.utils.TextColor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class DeckControl implements Get, Put {

    private Printer print;
    private TextColor textColor;
    private DeckSerializer deckSerializer;
    private DeckHandler deckHandler;
    private PlayerHandler playerHandler;
    private LoggerStatic loggerStatic;

    public DeckControl(){
        this.print = new Printer();
        this.textColor = new TextColor();
        this.playerHandler = new PlayerHandlerImpl();
        this.deckHandler = new DeckHandlerImpl();
        this.deckSerializer = new DeckSerializer();
        this.loggerStatic = LoggerStatic.getInstance();
    }

    /** --> LOAD DECK **/
    @Override
    public ResponseImpl get(RequestImpl requestImpl) {
        loggerStatic.log("\n\"# LOAD DECK\"\n");
       // System.out.println("# LOAD DECK");
        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }
        /** --> INSTANCE **/
        Player currentPlayer = this.playerHandler.getItemById(requestImpl.getAuth());

        if (currentPlayer.getDeck() == null) {
           // System.out.println(textColor.ANSI_RED + "DECK EMPTY" + textColor.ANSI_RESET);
            loggerStatic.log("\nDECK EMPTY\n");
            return new ResponseBuilderImpl().statusMethodNotAllowed(deckSerializer.message("DECK EMPTY").toString());
        }
        this.print.printDeck(currentPlayer.getDeck());
        loggerStatic.log("\n\"LOADING FINISHED!\"\n");
        //System.out.println(textColor.ANSI_GREEN + "LOADING FINISHED!" + textColor.ANSI_RESET);
        /** --> JSON OBJECT **/
        JsonObject jsonObject = deckSerializer.convertDeckToJson(currentPlayer.getDeck(),false,true,false);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }

    /** --> LOAD DECK FORMAT **/
    public ResponseImpl format(RequestImpl requestImpl) {
        loggerStatic.log("\nLOAD DECK\n");
        // System.out.println("# LOAD DECK");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerHandler.getItemById(requestImpl.getAuth());

        if (currentPlayer.getDeck() == null) {
            loggerStatic.log("\nDECK EMPTY\n");
            //System.out.println(textColor.ANSI_RED + "DECK EMPTY" + textColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(deckSerializer.message("DECK EMPTY").toString());
        }

        this.print.printDeck(currentPlayer.getDeck());
        loggerStatic.log("\nLOADING FINISHED!\n");
       // System.out.println(textColor.ANSI_GREEN + "LOADING FINISHED!" + textColor.ANSI_RESET);
        /** --> JSON OBJECT **/
        JsonObject jsonObject = deckSerializer.convertDeckToJson(currentPlayer.getDeck(),false,false,true);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }

    /*** --> SET DECK **/
    @Override
    public ResponseImpl put(RequestImpl requestImpl) {
        loggerStatic.log("\n## NEW DECK \n");
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
        Player currentPlayer = this.playerHandler.getItemById(requestImpl.getAuth());


        if (this.deckHandler.setNewDeck(newDeck, currentPlayer.getUserID())) {
            currentPlayer = playerHandler.reloadAccount(currentPlayer);
            loggerStatic.log("\nSET NEW DECK!\n");
          //  System.out.println(ANSI_GREEN + "SET NEW DECK!" + ANSI_RESET);
            /** --> JSON OBJECT **/
            JsonObject jsonObject = deckSerializer.convertDeckToJson(currentPlayer.getDeck(),true,false,false);
            /** --> STATUS OK **/
            return new ResponseBuilderImpl().statusOK(jsonObject.toString());
        }

        loggerStatic.log("\nSET NEW DECK - ERROR\n");
       // System.out.println(textColor.ANSI_RED + "SET NEW DECK - ERROR" + textColor.ANSI_RESET);
        return new ResponseBuilderImpl().statusMethodNotAllowed(deckSerializer.message("SET NEW DECK - ERROR").toString());
    }

}
