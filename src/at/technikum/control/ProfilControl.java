package at.technikum.control;

import at.technikum.model.Player;
import at.technikum.model.Profil;
import at.technikum.model.ProfilImpl;
import at.technikum.repository.PlayerRepository;
import at.technikum.repository.PlayerRepositoryImpl;
import at.technikum.repository.ProfilRepository;
import at.technikum.repository.ProfilRepositoryImpl;
import at.technikum.serializer.ProfilSerializer;
import at.technikum.server.utils.request.RequestImpl;
import at.technikum.server.utils.response.ResponseBuilderImpl;
import at.technikum.server.utils.response.ResponseImpl;
import at.technikum.utils.Printer;
import at.technikum.utils.PrinterImpl;
import at.technikum.utils.tools.TextColor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ProfilControl extends TextColor {

    Gson gson;
    Printer print;
    PlayerRepository playerRepository;
    ProfilRepository profilRepository;
    ProfilSerializer profilSerializer;

    public ProfilControl(){
        this.gson = new Gson();
        this.print = new PrinterImpl();
        this.playerRepository = new PlayerRepositoryImpl();
        this.profilRepository = new ProfilRepositoryImpl();
        this.profilSerializer = new ProfilSerializer();
    }

    /**
     * --> LOAD USER DATA
     **/
    public ResponseImpl get(RequestImpl requestImpl) {
        System.out.println("# USER Profil ");
        /** --> Wenn REQUEST Leer ist **/
        if (requestImpl == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new ResponseBuilderImpl().statusBAD(profilSerializer.message("BAD REQUEST").toString());
        }
        /** --> Wenn AUTH Leer ist **/
        if (requestImpl.getAuth() == null) {
            System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
            return new ResponseBuilderImpl().statusUnAuthorized(profilSerializer.message("NO TOKEN").toString());
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerRepository.getPlayerById(requestImpl.getAuth());


        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(profilSerializer.message("User NOT FOUND").toString());
        }
        /** -->  ERROR - MELDUNG USER NICHT AUTH **/
        if (!requestImpl.getPath().contains(currentPlayer.getUsername())) {
            System.out.println(ANSI_RED + "User NOT AUTH" + ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(profilSerializer.message("User NOT AUTH").toString());
        }

        Profil currentProfil = profilRepository.getProfilByID(currentPlayer.getUserID());
        this.print.printPlayerInfo(currentProfil);

        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        /** --> JSON OBJECT **/
        JsonObject jsonObject = profilSerializer.convertProfilToJson(currentProfil,true,true,true,true);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }

    /**
     * PLAYER INFO
     **/
    public ResponseImpl put(RequestImpl requestImpl) {
        System.out.println("#SET NEW PLAYER INFO ");

        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST --> WENN AUTH LEER IST **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, true);
        if (responseImpl != null) {
            return responseImpl;
        }


        /** --> INSTANCE **/
        Player currentPlayer = this.playerRepository.getPlayerById(requestImpl.getAuth());

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(profilSerializer.message("User NOT FOUND").toString());
        }
        /** -->  ERROR - MELDUNG USER NICHT AUTH **/
        if (!requestImpl.getPath().contains(currentPlayer.getUsername())) {
            System.out.println(ANSI_RED + "User NOT AUTH" + ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(profilSerializer.message("User NOT AUTH").toString());
        }


        /** --> Erstellt ein PLAYERINFO Objekt **/
        Profil profil = gson.fromJson(requestImpl.getBody(), ProfilImpl.class);
        profil.setUserID(currentPlayer.getUserID());

        /** -->  UPDATE: User fügt neue INFO zum ACCOUNT **/
        profilRepository.updateProfil(profil);

        /** --> läd Profil neu von der Datenbank **/
        Profil currentProfil = this.profilRepository.getProfilByID(currentPlayer.getUserID());

        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        /** --> JSON OBJECT **/
        JsonObject jsonObject = profilSerializer.convertProfilToJson(currentProfil,true,true,true,true);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }





}
