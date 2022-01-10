package at.technikum.control;

import at.technikum.repository.IPlayerRepository;
import at.technikum.repository.PlayerRepository;
import at.technikum.server.utils.request.Request;
import at.technikum.server.utils.response.Response;
import at.technikum.server.utils.response.ResponseBuilder;
import at.technikum.utils.IPrinter;
import at.technikum.utils.Printer;
import at.technikum.utils.card.service.CardServices;
import at.technikum.utils.card.service.ICardServices;
import at.technikum.repository.CardHolderRepository;
import at.technikum.repository.ICardHolderRepository;
import at.technikum.model.Deck;
import at.technikum.repository.DeckRepository;
import at.technikum.repository.IDeckRepository;
import at.technikum.model.IPlayer;
import at.technikum.model.Player;
import at.technikum.model.IProfil;
import at.technikum.model.Profil;
import at.technikum.repository.IProfilRepository;
import at.technikum.repository.ProfilRepository;
import at.technikum.model.Stack;
import at.technikum.repository.IStackRepository;
import at.technikum.repository.StackRepository;
import at.technikum.utils.tools.TextColor;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.regex.Pattern;

// https://www.delftstack.com/de/howto/java/java-json-to-object/
public class PlayerControl extends TextColor {


    IPrinter print;
    IPlayerRepository playerService;
    IProfilRepository playerInfoService;
    IStackRepository stackService;
    ICardHolderRepository cardHolderServices;
    ICardServices cardServices;
    IDeckRepository deckService;
    Gson gson;
    Pattern p;

    public PlayerControl() {
        gson = new Gson();
        p = Pattern.compile("/users/([a-zA-Z]+)/?");
        this.playerService = new PlayerRepository();
        this.playerInfoService = new ProfilRepository();
        this.cardHolderServices = new CardHolderRepository();
        this.cardServices = new CardServices();
        this.deckService = new DeckRepository();
        this.stackService = new StackRepository();
        this.print = new Printer();
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
        this.print.printDeck(currentPlayer.getDeck());
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

        this.print.printStack(currentPlayer.getStack());
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


        this.print.printPlayerInfo(playerInfoService.getInfoByID(currentPlayer.getUserID()));

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


        /** --> JSON OBJECT **/
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
