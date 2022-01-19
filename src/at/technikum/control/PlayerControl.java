package at.technikum.control;

import at.technikum.model.Player;
import at.technikum.model.PlayerImpl;
import at.technikum.repository.PlayerRepository;
import at.technikum.repository.PlayerRepositoryImpl;
import at.technikum.repository.ProfilRepository;
import at.technikum.repository.ProfilRepositoryImpl;
import at.technikum.serializer.PlayerSerializer;
import at.technikum.server.utils.request.RequestImpl;
import at.technikum.server.utils.response.ResponseBuilderImpl;
import at.technikum.server.utils.response.ResponseImpl;
import at.technikum.utils.Printer;
import at.technikum.utils.PrinterImpl;
import at.technikum.utils.tools.TextColor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.regex.Pattern;

// https://www.delftstack.com/de/howto/java/java-json-to-object/
public class PlayerControl extends TextColor {


    Printer print;
    PlayerRepository playerRepository;
    PlayerSerializer playerSerializer;
    ProfilRepository profilRepository;
    Gson gson;
    Pattern p;

    public PlayerControl() {
        this.gson = new Gson();
        this.playerRepository = new PlayerRepositoryImpl();
        this.profilRepository = new ProfilRepositoryImpl();
        this.playerSerializer = new PlayerSerializer();
        p = Pattern.compile("/users/([a-zA-Z]+)/?");
        this.print = new PrinterImpl();
    }

    /*** --> LOGIN **/
    public ResponseImpl login(RequestImpl requestImpl) {
       // System.out.println("# LOGIN ");
        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }
        /** --> Erstellt ein Player Objekt **/
        Player player = gson.fromJson(requestImpl.getBody(), PlayerImpl.class);
        /** --> User versucht sich einzuloggen **/
        Player currentPlayer = this.playerRepository.Login(player.getUsername(), player.getPassword());
        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(playerSerializer.message("User NOT FOUND").toString());
        }

        /** --> JSON OBJECT **/
        JsonObject jsonObject = playerSerializer.convertPlayerToJson(currentPlayer,true,true,false,false,false,false, false,false,false);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    } // TODO: 10.01.2022 Fertig

    /*** --> REGISTER**/
    public ResponseImpl register(RequestImpl requestImpl) {
      //  System.out.println("# REGISTER ");
        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> Erstellt ein Player Objekt **/
        Player newPlayer = gson.fromJson(requestImpl.getBody(), PlayerImpl.class);
        /** --> User versucht sich zu registrieren **/
        Player currentPlayer = this.playerRepository.Register(newPlayer.getUsername() + "-mtcgToken", newPlayer.getUsername(), newPlayer.getPassword());

        /** -->  ERROR - MELDUNG USER SCHON EXISTIERT **/
        if (currentPlayer == null) {
            return new ResponseBuilderImpl().statusMethodNotAllowed(playerSerializer.message("user already exists").toString());
        }

        /** --> Profil erstellen **/
        this.profilRepository.createProfil(currentPlayer);
        /** --> JSON OPJEKT **/
       JsonObject player = playerSerializer.convertPlayerToJson(currentPlayer,true,true,false,false,false,false,false, false,false);

        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(player.toString());
    } // TODO: 10.01.2022 Fertig

    /**
     * --> STATUS
     **/
    public ResponseImpl status(RequestImpl requestImpl) {
        System.out.println("# STATUS ");
        /** --> Wenn REQUEST Leer ist **/
        if (requestImpl == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new ResponseBuilderImpl().statusBAD(playerSerializer.message("BAD REQUEST").toString());
        }
        /** --> Wenn AUTH Leer ist **/
        if (requestImpl.getAuth() == null) {
            System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
            new ResponseBuilderImpl().statusUnAuthorized(playerSerializer.message("NO TOKEN").toString());
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerRepository.getPlayerById(requestImpl.getAuth());


        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(playerSerializer.message("User NOT FOUND").toString());
        }

        /** --> JSON OPJEKT **/
        JsonObject player = playerSerializer.convertPlayerToJson(currentPlayer,false,true,false,false,true,false,false, false,false);

        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(player.toString());
    }

    /*** --> SCORE**/
    public ResponseImpl highscore(RequestImpl requestImpl) {
        System.out.println("# SCORE ");
        /** --> Wenn REQUEST Leer ist **/
        if (requestImpl == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new ResponseBuilderImpl().statusBAD(playerSerializer.message("BAD REQUEST").toString());
        }
        /** --> Wenn AUTH Leer ist **/
        if (requestImpl.getAuth() == null) {
            System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
            new ResponseBuilderImpl().statusUnAuthorized(playerSerializer.message("NO TOKEN").toString());
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerRepository.getPlayerById(requestImpl.getAuth());


        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed("User NOT FOUND");
        }
        String list = playerRepository.getHighScoreList().toString();
        print.printHighscoreList(playerRepository.getHighScoreList());
        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        return new ResponseBuilderImpl().statusOK(playerSerializer.message(list).toString()); // TODO: 07.01.2022 HighScore Klasse
    }



}
