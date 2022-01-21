package at.technikum.control;

import at.technikum.control.repository.Get;
import at.technikum.control.repository.Post;
import at.technikum.logger.LoggerStatic;
import at.technikum.model.repository.Player;
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
import at.technikum.utils.TextColor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.regex.Pattern;

// https://www.delftstack.com/de/howto/java/java-json-to-object/
public class PlayerControl implements Post, Get {


    private Pattern p;
    private Gson gson;
    private Printer print;
    private TextColor textColor;
    private PlayerRepository playerRepository;
    private PlayerSerializer playerSerializer;
    private ProfilRepository profilRepository;
    private LoggerStatic loggerStatic;

    public PlayerControl(){
        this.gson = new Gson();
        this.textColor = new TextColor();
        this.playerRepository = new PlayerRepositoryImpl();
        this.profilRepository = new ProfilRepositoryImpl();
        this.playerSerializer = new PlayerSerializer();
        this.loggerStatic = LoggerStatic.getInstance();
        p = Pattern.compile("/users/([a-zA-Z]+)/?");
        this.print = new PrinterImpl();
    }

    /*** --> LOGIN **/
    @Override
    public ResponseImpl post(RequestImpl requestImpl) {
       // System.out.println("# LOGIN ");
        loggerStatic.log("\n# LOGIN\n");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, false, true, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> Erstellt ein Player Objekt **/
        Player player = gson.fromJson(requestImpl.getBody(), PlayerImpl.class);
        /** --> User versucht sich einzuloggen **/
        Player currentPlayer = this.playerRepository.Login(player.getUsername(), player.getPassword());

        /** --> JSON OBJECT **/
        JsonObject jsonObject = playerSerializer.convertPlayerToJson(currentPlayer,true,true,false,false,false,false, false,false,false);
        /** --> STATUS OK **/
        loggerStatic.log("\n LOGIN SUCCESS\n");
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }

    /*** --> REGISTER**/
    public ResponseImpl register(RequestImpl requestImpl) {
      //  System.out.println("# REGISTER ");
        loggerStatic.log("\n# REGISTER\n");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, false, true, false);
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
        loggerStatic.log("\n REGISTER SUCCESS\n");
        return new ResponseBuilderImpl().statusOK(player.toString());
    }

    /*** --> STATUS**/
    @Override
    public ResponseImpl get(RequestImpl requestImpl) {
       // System.out.println("# STATUS ");
        loggerStatic.log("\n # STATUS\n");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerRepository.getItemById(requestImpl.getAuth());

        /** --> JSON OPJEKT **/
        JsonObject player = playerSerializer.convertPlayerToJson(currentPlayer,false,true,false,false,true,false,false, false,false);

        /** --> STATUS OK **/
        loggerStatic.log("\nLOADING FINISHED!\n");
        return new ResponseBuilderImpl().statusOK(player.toString());
    }

    /*** --> SCORE**/
    public ResponseImpl highscore(RequestImpl requestImpl) {
       // System.out.println("# SCORE ");
        loggerStatic.log("\n # SCORE \n");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> INSTANCE **/
        //Player currentPlayer = this.playerRepository.getPlayerById(requestImpl.getAuth());

        String list = playerRepository.getHighScoreList().toString();
        print.printHighscoreList(playerRepository.getHighScoreList());
       // System.out.println(this.textColor.ANSI_GREEN + "LOADING FINISHED!" + this.textColor.ANSI_RESET);
        loggerStatic.log("\nLOADING FINISHED!\n");
        return new ResponseBuilderImpl().statusOK(playerSerializer.message(list).toString()); // TODO: 07.01.2022 HighScore Klasse
    }



}
