package at.technikum.utils.player.service;


import at.technikum.server.repository.Repository;
import at.technikum.server.request.Request;
import at.technikum.server.response.Response;
import at.technikum.utils.card.service.CardServices;
import at.technikum.utils.card.service.ICardServices;
import at.technikum.utils.cardHolder.service.CardHolderServices;
import at.technikum.utils.cardHolder.service.ICardHolderServices;
import at.technikum.utils.deck.service.DeckService;
import at.technikum.utils.deck.service.IDeckService;
import at.technikum.utils.player.IPlayer;
import at.technikum.utils.player.Player;
import at.technikum.utils.stack.service.IStackService;
import at.technikum.utils.stack.service.StackService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.regex.Pattern;

// https://www.delftstack.com/de/howto/java/java-json-to-object/
public class PlayerServlet extends Repository {


    IPlayerService playerService;
   // IPlayerInfoService playerInfoService;
    IStackService stackService;
    ICardHolderServices cardHolderServices;
    ICardServices cardServices;
    IDeckService deckService;
    Gson gson;
    Pattern p;

    public PlayerServlet() {
        gson = new Gson();
        p = Pattern.compile("/users/([a-zA-Z]+)/?");
        this.playerService = new PlayerService();
      //  this.playerInfoService = new PlayerInfoService();
        this.cardHolderServices = new CardHolderServices();
        this.cardServices = new CardServices();
        this.deckService = new DeckService();
        this.stackService = new StackService();
    }

    /**
     * --> LOAD DECK
     **/
    public Response DECK(Request request) {
        System.out.println("# LOAD DECK");

        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST **/
        Response response = new Response().requestErrorHandler(request, true, false);
        if (response != null) {
            return response;
        }

        /** --> INSTANCE **/
        IPlayer currentPlayer = this.playerService.getPlayerById(request.getAuth());

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "USER NOT FOUND" + ANSI_RESET);
            return new Response().statusMethodNotAllowed();
        }
        if (currentPlayer.getDeck() == null) {
            System.out.println(ANSI_RED + "DECK EMPTY" + ANSI_RESET);
            return new Response().statusMethodNotAllowed();
        }
        System.out.println("START");
        this.deckService.printDeck(currentPlayer.getDeck());
        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        return new Response().statusOK();
    }

    /**
     * --> SET DECK
     **/
    public Response SETDECK(Request request) {
        System.out.println("#NEW DECK ");

        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST --> WENN AUTH LEER IST **/
        Response response = new Response().requestErrorHandler(request, true, true);
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
        IPlayer currentPlayer = this.playerService.getPlayerById(request.getAuth());
        if (this.deckService.setNewDeck(newDeck, currentPlayer.getUserID())) {
            System.out.println(ANSI_GREEN + "SET NEW DECK!" + ANSI_RESET);
            return new Response().statusCreated();
        }
        this.deckService.setNewDeck(newDeck, currentPlayer.getUserID());

        System.out.println(ANSI_RED + "SET NEW DECK - ERROR" + ANSI_RESET);
        return new Response().statusMethodNotAllowed();
    }


    /**
     * --> LOAD STACK
     **/
    public Response STACK(Request request) {
        System.out.println("# STACK ");
        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new Response().statusBAD();
        }
        /** --> Wenn AUTH Leer ist **/
        if (request.getAuth() == null) {
            System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
            return new Response().statusUnAuthorized();
        }

        /** --> INSTANCE **/
        IPlayer currentPlayer = this.playerService.getPlayerById(request.getAuth());


        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new Response().statusMethodNotAllowed();
        }

        stackService.printStack(currentPlayer.getStack());
        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        return new Response().statusOK();
    }


    /**
     * --> LOAD USER DATA
     **/
    @Override
    public Response GET(Request request) {
        System.out.println("# USER DATA ");
        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new Response().statusBAD();
        }
        /** --> Wenn AUTH Leer ist **/
        if (request.getAuth() == null) {
            System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
            return new Response().statusUnAuthorized();
        }

        /** --> INSTANCE **/
        IPlayer currentPlayer = this.playerService.getPlayerById(request.getAuth());


        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new Response().statusMethodNotAllowed();
        }
        /** -->  ERROR - MELDUNG USER NICHT AUTH **/
        if (!request.getPath().contains(currentPlayer.getUsername())) {
            System.out.println(ANSI_RED + "User NOT AUTH" + ANSI_RESET);
            return new Response().statusMethodNotAllowed();
        }


     //   this.playerInfoService.printPlayerInfo(playerInfoService.getInfoByID(currentPlayer.getUserID()));
        System.out.println();
        this.playerService.printPlayerData(currentPlayer);
        System.out.println();
        this.stackService.printStack(currentPlayer.getStack());
        System.out.println();
        this.deckService.printDeck(currentPlayer.getDeck());

        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        return new Response().statusOK();
    }

    /**
     * --> LOGIN
     **/
    public Response LOGIN(Request request) {
        System.out.println("# LOGIN ");
        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST **/
        Response response = new Response().requestErrorHandler(request, false, true);
        if (response != null) {
            return response;
        }

        /** --> Erstellt ein Player Objekt **/
        IPlayer player = gson.fromJson(request.getBody(), Player.class);
        /** --> User versucht sich einzuloggen **/
        IPlayer currentPlayer = this.playerService.Login(player.getUsername(), player.getPassword());
        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new Response().statusMethodNotAllowed();
        }
        /** --> STATUS OK **/
        return new Response().statusOK();
    }

    /**
     * --> REGISTER
     **/
    @Override
    public Response POST(Request request) {
        System.out.println("# REGISTER ");
        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST **/
        Response response = new Response().requestErrorHandler(request, false, true);
        if (response != null) {
            return response;
        }

        /** --> Erstellt ein Player Objekt **/
        IPlayer newPlayer = gson.fromJson(request.getBody(), Player.class);
        /** --> User versucht sich zu registrieren **/
        IPlayer currentPlayer = this.playerService.Register("Basic " + newPlayer.getUsername() + "-mtcgToken", newPlayer.getUsername(), newPlayer.getPassword());
        /** -->  ERROR - MELDUNG USER SCHON EXISTIERT **/
        if (currentPlayer == null) {
            return new Response().statusMethodNotAllowed();
        }
        /** --> STATUS OK **/
        return new Response().statusOK();
    }


    /**
     * --> SCORE
     **/
    public Response SCORE(Request request) {
        System.out.println("# SCORE ");
        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new Response().statusBAD();
        }
        /** --> Wenn AUTH Leer ist **/
        if (request.getAuth() == null) {
            System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
            return new Response().statusUnAuthorized();
        }

        /** --> INSTANCE **/
        IPlayer currentPlayer = this.playerService.getPlayerById(request.getAuth());


        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new Response().statusMethodNotAllowed();
        }

        System.out.println("USERNAME: " + currentPlayer.getUsername());
        System.out.println("SCORE: " + currentPlayer.getElo());
        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        return new Response().statusOK();
    }

    /**
     * --> STATUS
     **/
    public Response STATUS(Request request) {
        System.out.println("# STATUS ");
        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new Response().statusBAD();
        }
        /** --> Wenn AUTH Leer ist **/
        if (request.getAuth() == null) {
            System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
            return new Response().statusUnAuthorized();
        }

        /** --> INSTANCE **/
        IPlayer currentPlayer = this.playerService.getPlayerById(request.getAuth());


        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new Response().statusMethodNotAllowed();
        }

        System.out.println("USERNAME: " + currentPlayer.getUsername());
        System.out.println("STATUS: " + currentPlayer.isStatus());
        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        return new Response().statusOK();
    }


    /**
     * PLAYER INFO
     **/
    @Override
    public Response PUT(Request request) {
        System.out.println("#SET NEW PLAYER INFO ");

        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST --> WENN AUTH LEER IST **/
        Response response = new Response().requestErrorHandler(request, true, true);
        if (response != null) {
            return response;
        }


        /** --> INSTANCE **/
        IPlayer currentPlayer = this.playerService.getPlayerById(request.getAuth());

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new Response().statusMethodNotAllowed();
        }
        /** -->  ERROR - MELDUNG USER NICHT AUTH **/
        if (!request.getPath().contains(currentPlayer.getUsername())) {
            System.out.println(ANSI_RED + "User NOT AUTH" + ANSI_RESET);
            return new Response().statusMethodNotAllowed();
        }


        /** --> Erstellt ein PLAYERINFO Objekt **/
     //   IPlayerInfo playerInfo = gson.fromJson(request.getBody(), PlayerInfo.class);
      //  playerInfo.setUserID(currentPlayer.getUserID());


        /** --> User fügt neue INFO zum ACCOUNT **/

        //playerInfoService.setInfo(playerInfo);
        System.out.println(ANSI_GREEN + "FINISHED" + ANSI_RESET);
        return new Response().statusOK();
    }


    @Override
    public Response INDEX(Request request) {
        if (request == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new Response().statusBAD();
        }
        return new Response().statusOK();
    }

    @Override
    public Response DELETE(Request request) {
        if (request == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new Response().statusBAD();
        }
        return new Response().statusOK();
    }
}
