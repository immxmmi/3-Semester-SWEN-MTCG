package at.technikum.control;

import at.technikum.model.*;
import at.technikum.repository.*;
import at.technikum.server.utils.request.Request;
import at.technikum.server.utils.response.Response;
import at.technikum.server.utils.response.ResponseBuilder;
import at.technikum.utils.IPrinter;
import at.technikum.utils.Printer;
import at.technikum.utils.card.service.CardServices;
import at.technikum.utils.card.service.ICardServices;
import at.technikum.utils.tools.TextColor;
import com.google.gson.*;

import java.util.regex.Pattern;

// https://www.delftstack.com/de/howto/java/java-json-to-object/
public class PlayerControl extends TextColor {


    IPrinter print;
    PlayerRepository playerRepository;
    ProfilRepository playerInfoService;
    StackRepository stackService;
    CardHolderRepository cardHolderServices;
    ICardServices cardServices;
    DeckRepository deckService;
    Gson gson;
    Pattern p;

    public PlayerControl() {
        gson = new Gson();
        p = Pattern.compile("/users/([a-zA-Z]+)/?");
        this.playerRepository = new PlayerRepositoryImpl();
        this.playerInfoService = new ProfilRepositoryImpl();
        this.cardHolderServices = new CardHolderRepositoryImpl();
        this.cardServices = new CardServices();
        this.deckService = new DeckRepositoryImpl();
        this.stackService = new StackRepositoryImpl();
        this.print = new Printer();
    }






    /**
     * --> LOAD USER DATA
     **/
    public Response PROFIL(Request request) {
        System.out.println("# USER Profil ");
        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new ResponseBuilder().statusBAD(this.message("BAD REQUEST").toString());
        }
        /** --> Wenn AUTH Leer ist **/
        if (request.getAuth() == null) {
            System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
            return new ResponseBuilder().statusUnAuthorized(this.message("NO TOKEN").toString());
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerRepository.getPlayerById(request.getAuth());


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
        return new ResponseBuilder().statusOK(gson.toJson(currentPlayer, ProfilImpl.class));
    }

    /*** --> LOGIN **/
    public Response login(Request request) {
        System.out.println("# LOGIN ");
        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST **/
        Response response = new ResponseBuilder().requestErrorHandler(request, false, true);
        if (response != null) {
            return response;
        }
        /** --> Erstellt ein Player Objekt **/
        Player player = gson.fromJson(request.getBody(), PlayerImpl.class);
        /** --> User versucht sich einzuloggen **/
        Player currentPlayer = this.playerRepository.Login(player.getUsername(), player.getPassword());
        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed(this.message("User NOT FOUND").toString());
        }


        /** --> JSON OBJECT **/
        JsonObject jsonObject = convertPlayerToJson(currentPlayer,false,true,false,false,false,false, false,false,false);
        /** --> STATUS OK **/
        return new ResponseBuilder().statusOK(jsonObject.toString());
    } // TODO: 10.01.2022 Fertig

    /*** --> REGISTER**/
    public Response register(Request request) {
        System.out.println("# REGISTER ");
        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST **/
        Response response = new ResponseBuilder().requestErrorHandler(request, false, true);
        if (response != null) {
            return response;
        }

        /** --> Erstellt ein Player Objekt **/
        Player newPlayer = gson.fromJson(request.getBody(), PlayerImpl.class);
        /** --> User versucht sich zu registrieren **/
        Player currentPlayer = this.playerRepository.Register(newPlayer.getUsername() + "-mtcgToken", newPlayer.getUsername(), newPlayer.getPassword());

        /** -->  ERROR - MELDUNG USER SCHON EXISTIERT **/
        if (currentPlayer == null) {
            return new ResponseBuilder().statusMethodNotAllowed(this.message("user already exists").toString());
        }

       JsonObject player = convertPlayerToJson(currentPlayer,true,true,false,false,false,false,false, false,false);

        /** --> STATUS OK **/
        return new ResponseBuilder().statusOK(player.toString());
    } // TODO: 10.01.2022 Fertig

    /** Wandelt Java Player-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    private JsonObject convertPlayerToJson(Player player, boolean userID, boolean Username, boolean Password, boolean coins, boolean status, boolean stack,  boolean deck,boolean freeStack, boolean elo){
        JsonObject object = new JsonParser().parse(gson.toJson(player, PlayerImpl.class)).getAsJsonObject();
        if(!userID){object.remove("userID");}
        if(!Username){object.remove("Username");}
        if(!Password){object.remove("Password");}
        if(!coins){object.remove("coins");}
        if(!status){object.remove("status");}
        if(!stack){object.remove("stack");}
        if(!deck){object.remove("deck");}
        if(!freeStack){object.remove("freeStack");}
        if(!elo){object.remove("elo");}
        object.remove("tokenSupplier");
        object.remove("token");
        return object;
    } // TODO: 10.01.2022 Fertig

    /** --> wandelt message String in JSON-Objekt um **/
    private JsonObject message(String message){
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
        Player currentPlayer = this.playerRepository.getPlayerById(request.getAuth());

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
        Profil profil = gson.fromJson(request.getBody(), ProfilImpl.class);
        profil.setUserID(currentPlayer.getUserID());


        /** --> User fügt neue INFO zum ACCOUNT **/

        playerInfoService.setInfo(profil);
        System.out.println(ANSI_GREEN + "FINISHED" + ANSI_RESET);
        return new ResponseBuilder().statusOK(gson.toJson(profil, ProfilImpl.class));
    }






}
