package at.technikum.control;

import at.technikum.control.repository.Get;
import at.technikum.control.repository.Put;
import at.technikum.logger.LoggerStatic;
import at.technikum.model.repository.Player;
import at.technikum.model.repository.Profil;
import at.technikum.model.ProfilImpl;
import at.technikum.handler.repository.PlayerHandler;
import at.technikum.handler.PlayerHandlerImpl;
import at.technikum.handler.repository.ProfilHandler;
import at.technikum.handler.ProfilHandlerImpl;
import at.technikum.serializer.ProfilSerializer;
import at.technikum.server.request.RequestImpl;
import at.technikum.server.response.ResponseBuilderImpl;
import at.technikum.server.response.ResponseImpl;
import at.technikum.utils.Printer;
import at.technikum.utils.PrinterImpl;
import at.technikum.utils.TextColor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ProfilControl implements Put,Get {

    private Gson gson;
    private Printer print;
    private TextColor textColor;
    private PlayerHandler playerHandler;
    private ProfilHandler profilHandler;
    private ProfilSerializer profilSerializer;
    private LoggerStatic loggerStatic;

    public ProfilControl(){
        this.gson = new Gson();
        this.print = new PrinterImpl();
        this.textColor = new TextColor();
        this.playerHandler = new PlayerHandlerImpl();
        this.profilHandler = new ProfilHandlerImpl();
        this.profilSerializer = new ProfilSerializer();
        this.loggerStatic = LoggerStatic.getInstance();
    }

    /**--> LOAD USER DATA**/
    @Override
    public ResponseImpl get(RequestImpl requestImpl) {
        //System.out.println("# Profil ");
        loggerStatic.log("\n# Profil \n");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }


        /** --> INSTANCE **/
        Player currentPlayer = this.playerHandler.getItemById(requestImpl.getAuth());


        /** -->  ERROR - MELDUNG USER NICHT AUTH **/
        if (!requestImpl.getPath().contains(currentPlayer.getUsername())) {
           // System.out.println(this.textColor.ANSI_RED + "USER NOT AUTH" + this.textColor.ANSI_RESET);
            loggerStatic.log("\nUSER NOT AUTH\n");
            return new ResponseBuilderImpl().statusMethodNotAllowed(profilSerializer.message("User NOT AUTH").toString());
        }

        Profil currentProfil = profilHandler.getItemById(currentPlayer.getUserID());
        this.print.printPlayerInfo(currentProfil);

        //System.out.println(this.textColor.ANSI_GREEN + "LOADING FINISHED!" + this.textColor.ANSI_RESET);
        loggerStatic.log("\nLOADING FINISHED!\n");
        /** --> JSON OBJECT **/
        JsonObject jsonObject = profilSerializer.convertProfilToJson(currentProfil,true,true,true,true);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }

    /*** PLAYER INFO**/
    @Override
    public ResponseImpl put(RequestImpl requestImpl) {
       // System.out.println("#SET NEW PLAYER INFO ");
        loggerStatic.log("\n#SET NEW PLAYER INFO \n");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, true, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerHandler.getItemById(requestImpl.getAuth());

        /** -->  ERROR - MELDUNG USER NICHT AUTH **/
        if (!requestImpl.getPath().contains(currentPlayer.getUsername())) {
            //System.out.println(this.textColor.ANSI_RED + "User NOT AUTH" + this.textColor.ANSI_RESET);
            loggerStatic.log("\nUSER NOT AUTH \n");
            return new ResponseBuilderImpl().statusMethodNotAllowed(profilSerializer.message("User NOT AUTH").toString());
        }


        /** --> Erstellt ein PLAYERINFO Objekt **/
        Profil profil = gson.fromJson(requestImpl.getBody(), ProfilImpl.class);
        profil.setUserID(currentPlayer.getUserID());

        /** -->  UPDATE: User fügt neue INFO zum ACCOUNT **/
        profilHandler.updateProfil(profil);

        /** --> läd Profil neu von der Datenbank **/
        Profil currentProfil = this.profilHandler.getItemById(currentPlayer.getUserID());

        loggerStatic.log("\nLOADING FINISHED! \n");
        //System.out.println(this.textColor.ANSI_GREEN + "LOADING FINISHED!" + this.textColor.ANSI_RESET);
        /** --> JSON OBJECT **/
        JsonObject jsonObject = profilSerializer.convertProfilToJson(currentProfil,true,true,true,true);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }

}
