package at.technikum.control;

import at.technikum.model.Player;
import at.technikum.model.Profil;
import at.technikum.model.ProfilImpl;
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

public class ProfilControl extends TextColor {

    Gson gson;
    IPrinter print;
    PlayerRepository playerRepository;
    ProfilRepository profilRepository;

    public ProfilControl(){
        this.gson = new Gson();
        this.print = new Printer();
        this.playerRepository = new PlayerRepositoryImpl();
        this.profilRepository = new ProfilRepositoryImpl();
    }

    /**
     * --> LOAD USER DATA
     **/
    public Response get(Request request) {
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
            return new ResponseBuilder().statusMethodNotAllowed(this.message("User NOT FOUND").toString());
        }
        /** -->  ERROR - MELDUNG USER NICHT AUTH **/
        if (!request.getPath().contains(currentPlayer.getUsername())) {
            System.out.println(ANSI_RED + "User NOT AUTH" + ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed(this.message("User NOT AUTH").toString());
        }

        Profil currentProfil = profilRepository.getProfilByID(currentPlayer.getUserID());
        this.print.printPlayerInfo(currentProfil);

        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        /** --> JSON OBJECT **/
        JsonObject jsonObject = convertProfilToJson(currentProfil,true,true,true,true);
        /** --> STATUS OK **/
        return new ResponseBuilder().statusOK(jsonObject.toString());
    }

    /**
     * PLAYER INFO
     **/
    public Response put(Request request) {
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
            return new ResponseBuilder().statusMethodNotAllowed(this.message("User NOT FOUND").toString());
        }
        /** -->  ERROR - MELDUNG USER NICHT AUTH **/
        if (!request.getPath().contains(currentPlayer.getUsername())) {
            System.out.println(ANSI_RED + "User NOT AUTH" + ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed(this.message("User NOT AUTH").toString());
        }


        /** --> Erstellt ein PLAYERINFO Objekt **/
        Profil profil = gson.fromJson(request.getBody(), ProfilImpl.class);
        profil.setUserID(currentPlayer.getUserID());

        /** -->  UPDATE: User fügt neue INFO zum ACCOUNT **/
        profilRepository.updateProfil(profil);

        /** --> läd Profil neu von der Datenbank **/
        Profil currentProfil = this.profilRepository.getProfilByID(currentPlayer.getUserID());

        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        /** --> JSON OBJECT **/
        JsonObject jsonObject = convertProfilToJson(currentProfil,true,true,true,true);
        /** --> STATUS OK **/
        return new ResponseBuilder().statusOK(jsonObject.toString());
    }


    /** Wandelt Java Player-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    private JsonObject convertProfilToJson(Profil profil, boolean userID, boolean name, boolean bio, boolean image){
        JsonObject object = new JsonParser().parse(gson.toJson(profil, ProfilImpl.class)).getAsJsonObject();
        if(!userID){object.remove("userID");}
        if(!name){object.remove("Name");}
        if(!bio){object.remove("Bio");}
        if(!image){object.remove("Image");}
        return object;
    } // TODO: 10.01.2022 Fertig

    /** --> wandelt message String in JSON-Objekt um **/
    private JsonObject message(String message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Message",message);
        return jsonObject;
    } // TODO: 10.01.2022 Fertig


}
