package at.technikum.utils.player.service;

import at.technikum.server.utils.request.Request;
import at.technikum.server.utils.response.Response;
import at.technikum.server.utils.response.ResponseBuilder;
import at.technikum.utils.card.service.CardServices;
import at.technikum.utils.card.service.ICardServices;
import at.technikum.utils.cardHolder.service.CardHolderServices;
import at.technikum.utils.cardHolder.service.ICardHolderServices;
import at.technikum.utils.deck.Deck;
import at.technikum.utils.deck.service.DeckService;
import at.technikum.utils.deck.service.IDeckService;
import at.technikum.utils.player.IPlayer;
import at.technikum.utils.player.Player;
import at.technikum.database.model.IProfil;
import at.technikum.database.model.Profil;
import at.technikum.utils.profil.IProfilService;
import at.technikum.utils.profil.ProfilService;
import at.technikum.utils.stack.Stack;
import at.technikum.utils.stack.service.IStackService;
import at.technikum.utils.stack.service.StackService;
import at.technikum.utils.tools.TextColor;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.regex.Pattern;

// https://www.delftstack.com/de/howto/java/java-json-to-object/
public class PlayerControl extends TextColor {


    IPlayerService playerService;
    IProfilService playerInfoService;
    IStackService stackService;
    ICardHolderServices cardHolderServices;
    ICardServices cardServices;
    IDeckService deckService;
    Gson gson;
    Pattern p;

    public PlayerControl() {
        gson = new Gson();
        p = Pattern.compile("/users/([a-zA-Z]+)/?");
        this.playerService = new PlayerService();
        this.playerInfoService = new ProfilService();
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
        Response response = new ResponseBuilder().requestErrorHandler(request, true, false);
        if (response != null) {
            return response;
        }

        /** --> INSTANCE **/
        IPlayer currentPlayer = this.playerService.getPlayerById(request.getAuth());

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "USER NOT FOUND" + ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed("USER NOT FOUND");
        }
        if (currentPlayer.getDeck() == null) {
            System.out.println(ANSI_RED + "DECK EMPTY" + ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed("DECK EMPTY");
        }
        System.out.println("START");
        this.deckService.printDeck(currentPlayer.getDeck());
        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        return new ResponseBuilder().statusOK(gson.toJson(currentPlayer.getDeck(), Deck.class));
    }

    /**
     * --> SET DECK
     **/
    public Response SETDECK(Request request) {
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
        IPlayer currentPlayer = this.playerService.getPlayerById(request.getAuth());
        if (this.deckService.setNewDeck(newDeck, currentPlayer.getUserID())) {
            System.out.println(ANSI_GREEN + "SET NEW DECK!" + ANSI_RESET);
            return new ResponseBuilder().statusOK(gson.toJson(currentPlayer.getDeck(),Deck.class));
        }
        this.deckService.setNewDeck(newDeck, currentPlayer.getUserID());

        System.out.println(ANSI_RED + "SET NEW DECK - ERROR" + ANSI_RESET);
        return new ResponseBuilder().statusMethodNotAllowed("SET NEW DECK - ERROR");
    }


    /**
     * --> LOAD STACK
     **/
    public Response STACK(Request request) {
        System.out.println("# STACK ");
        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new ResponseBuilder().statusBAD(this.Message("BAD REQUEST").toString());
        }
        /** --> Wenn AUTH Leer ist **/
        if (request.getAuth() == null) {
            System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
            new ResponseBuilder().statusUnAuthorized(this.Message("NO TOKEN").toString());
        }

        /** --> INSTANCE **/
        IPlayer currentPlayer = this.playerService.getPlayerById(request.getAuth());


        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed("User NOT FOUND");
        }

        stackService.printStack(currentPlayer.getStack());
        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        return new ResponseBuilder().statusOK(gson.toJson(currentPlayer.getStack(),Stack.class));
    }


    /**
     * --> LOAD USER DATA
     **/
    public Response PROFIL(Request request) {
        System.out.println("# USER Profil ");
        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new ResponseBuilder().statusBAD(this.Message("BAD REQUEST").toString());
        }
        /** --> Wenn AUTH Leer ist **/
        if (request.getAuth() == null) {
            System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
            return new ResponseBuilder().statusUnAuthorized(this.Message("NO TOKEN").toString());
        }

        /** --> INSTANCE **/
        IPlayer currentPlayer = this.playerService.getPlayerById(request.getAuth());


        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed("User NOT FOUND");
        }
        /** -->  ERROR - MELDUNG USER NICHT AUTH **/
        if (!request.getPath().contains(currentPlayer.getUsername())) {
            System.out.println(ANSI_RED + "User NOT AUTH" + ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed("User NOT AUTH");
        }


        this.playerInfoService.printPlayerInfo(playerInfoService.getInfoByID(currentPlayer.getUserID()));

        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        return new ResponseBuilder().statusOK(gson.toJson(currentPlayer,Profil.class));
    }

    /*** --> LOGIN **/
    public Response LOGIN(Request request) {
        System.out.println("# LOGIN ");
        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST **/
        Response response = new ResponseBuilder().requestErrorHandler(request, false, true);
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

            return new ResponseBuilder().statusMethodNotAllowed(this.Message("User NOT FOUND").toString());
        }
        JsonObject jsonObject = convertPlayerToJson(currentPlayer,true,true,false,false,true,false,false,false);
        /** --> STATUS OK **/
        return new ResponseBuilder().statusOK(jsonObject.toString());
    } // TODO: 10.01.2022 Fertig

    /*** --> REGISTER**/
    public Response REGISTER(Request request) {
        System.out.println("# REGISTER ");
        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST **/
        Response response = new ResponseBuilder().requestErrorHandler(request, false, true);
        if (response != null) {
            return response;
        }

        /** --> Erstellt ein Player Objekt **/
        IPlayer newPlayer = gson.fromJson(request.getBody(), Player.class);
        /** --> User versucht sich zu registrieren **/
        IPlayer currentPlayer = this.playerService.Register(newPlayer.getUsername() + "-mtcgToken", newPlayer.getUsername(), newPlayer.getPassword());

        /** -->  ERROR - MELDUNG USER SCHON EXISTIERT **/
        if (currentPlayer == null) {
            return new ResponseBuilder().statusMethodNotAllowed(this.Message("user already exists").toString());
        }

       JsonObject player = convertPlayerToJson(currentPlayer,true,true,false,false,false,false,false,false);

        /** --> STATUS OK **/
        return new ResponseBuilder().statusOK(player.toString());
    } // TODO: 10.01.2022 Fertig

    /** Wandelt Java Player-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    private JsonObject convertPlayerToJson(IPlayer player,boolean userID,boolean Username,boolean Password,boolean coins,boolean status,boolean stack,boolean freeStack,boolean elo){
        JsonObject object = new JsonParser().parse(gson.toJson(player,Player.class)).getAsJsonObject();
        if(!userID){object.remove("userID");}
        if(!Username){object.remove("Username");}
        if(!Password){object.remove("Password");}
        if(!coins){object.remove("coins");}
        if(!status){object.remove("status");}
        if(!stack){object.remove("stack");}
        if(!freeStack){object.remove("freeStack");}
        if(!elo){object.remove("elo");}
        object.remove("tokenSupplier");
        object.remove("token");
        return object;
    } // TODO: 10.01.2022 Fertig

    /** --> wandelt message String in JSON-Objekt um **/
    private JsonObject Message(String message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Message",message);
        return jsonObject;
    } // TODO: 10.01.2022 Fertig 














    /**
     * --> SCORE
     **/
    public Response SCORE(Request request) {
        System.out.println("# SCORE ");
        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new ResponseBuilder().statusBAD(this.Message("BAD REQUEST").toString());
        }
        /** --> Wenn AUTH Leer ist **/
        if (request.getAuth() == null) {
            System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
            new ResponseBuilder().statusUnAuthorized(this.Message("NO TOKEN").toString());
        }

        /** --> INSTANCE **/
        IPlayer currentPlayer = this.playerService.getPlayerById(request.getAuth());


        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed("User NOT FOUND");
        }

        System.out.println("USERNAME: " + currentPlayer.getUsername());
        System.out.println("SCORE: " + currentPlayer.getElo());
        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        return new ResponseBuilder().statusOK("LOADING FINISHED!"); // TODO: 07.01.2022 HighScore Klasse
    }

    /**
     * --> STATUS
     **/
    public Response STATUS(Request request) {
        System.out.println("# STATUS ");
        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new ResponseBuilder().statusBAD(this.Message("BAD REQUEST").toString());
        }
        /** --> Wenn AUTH Leer ist **/
        if (request.getAuth() == null) {
            System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
            new ResponseBuilder().statusUnAuthorized(this.Message("NO TOKEN").toString());
        }

        /** --> INSTANCE **/
        IPlayer currentPlayer = this.playerService.getPlayerById(request.getAuth());


        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed("User NOT FOUND");
        }

        System.out.println("USERNAME: " + currentPlayer.getUsername());
        System.out.println("STATUS: " + currentPlayer.isStatus());
        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        return new ResponseBuilder().statusOK("LOADING FINISHED!");
    }


    /**
     * PLAYER INFO
     **/
    public Response PUT(Request request) {
        System.out.println("#SET NEW PLAYER INFO ");

        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST --> WENN AUTH LEER IST **/
        Response response = new ResponseBuilder().requestErrorHandler(request, true, true);
        if (response != null) {
            return response;
        }


        /** --> INSTANCE **/
        IPlayer currentPlayer = this.playerService.getPlayerById(request.getAuth());

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed("User NOT FOUND");
        }
        /** -->  ERROR - MELDUNG USER NICHT AUTH **/
        if (!request.getPath().contains(currentPlayer.getUsername())) {
            System.out.println(ANSI_RED + "User NOT AUTH" + ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed("User NOT AUTH");
        }


        /** --> Erstellt ein PLAYERINFO Objekt **/
        IProfil profil = gson.fromJson(request.getBody(), Profil.class);
        profil.setUserID(currentPlayer.getUserID());


        /** --> User fügt neue INFO zum ACCOUNT **/

        playerInfoService.setInfo(profil);
        System.out.println(ANSI_GREEN + "FINISHED" + ANSI_RESET);
        return new ResponseBuilder().statusOK(gson.toJson(profil,Profil.class));
    }




}
