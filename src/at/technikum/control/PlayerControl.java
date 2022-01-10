package at.technikum.control;

import at.technikum.model.Player;
import at.technikum.model.PlayerImpl;
import at.technikum.repository.PlayerRepository;
import at.technikum.repository.PlayerRepositoryImpl;
import at.technikum.repository.ProfilRepository;
import at.technikum.repository.ProfilRepositoryImpl;
import at.technikum.server.utils.request.Request;
import at.technikum.server.utils.response.Response;
import at.technikum.server.utils.response.ResponseBuilder;
import at.technikum.utils.IPrinter;
import at.technikum.utils.Printer;
import at.technikum.utils.tools.TextColor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.regex.Pattern;

// https://www.delftstack.com/de/howto/java/java-json-to-object/
public class PlayerControl extends TextColor {


    IPrinter print;
    PlayerRepository playerRepository;
    ProfilRepository profilRepository;
    Gson gson;
    Pattern p;

    public PlayerControl() {
        this.gson = new Gson();
        this.playerRepository = new PlayerRepositoryImpl();
        this.profilRepository = new ProfilRepositoryImpl();
        p = Pattern.compile("/users/([a-zA-Z]+)/?");
        this.print = new Printer();
    }

    /*** --> LOGIN **/
    public Response login(Request request) {
       // System.out.println("# LOGIN ");
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
        JsonObject jsonObject = convertPlayerToJson(currentPlayer,true,true,false,false,false,false, false,false,false);
        /** --> STATUS OK **/
        return new ResponseBuilder().statusOK(jsonObject.toString());
    } // TODO: 10.01.2022 Fertig

    /*** --> REGISTER**/
    public Response register(Request request) {
      //  System.out.println("# REGISTER ");
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

        /** --> Profil erstellen **/
        this.profilRepository.createProfil(currentPlayer);
        /** --> JSON OPJEKT **/
       JsonObject player = convertPlayerToJson(currentPlayer,true,true,false,false,false,false,false, false,false);

        /** --> STATUS OK **/
        return new ResponseBuilder().statusOK(player.toString());
    } // TODO: 10.01.2022 Fertig

    /**
     * --> STATUS
     **/
    public Response status(Request request) {
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
            return new ResponseBuilder().statusMethodNotAllowed(this.message("User NOT FOUND").toString());
        }

        /** --> JSON OPJEKT **/
        JsonObject player = convertPlayerToJson(currentPlayer,false,true,false,false,true,false,false, false,false);

        /** --> STATUS OK **/
        return new ResponseBuilder().statusOK(player.toString());
    }

    /**
     * --> SCORE
     **/
    public Response highscore(Request request) {
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

        String list = playerRepository.getHighScoreList().toString();
        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        return new ResponseBuilder().statusOK(this.message(list).toString()); // TODO: 07.01.2022 HighScore Klasse
    }

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

}
