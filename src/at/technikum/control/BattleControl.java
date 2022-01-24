package at.technikum.control;

import at.technikum.control.repository.Post;
import at.technikum.handler.BattleHandlerImpl;
import at.technikum.handler.PlayerHandlerImpl;
import at.technikum.handler.repository.BattleHandler;
import at.technikum.handler.repository.PlayerHandler;
import at.technikum.logger.LoggerStatic;
import at.technikum.model.repository.Battle;
import at.technikum.model.repository.Player;
import at.technikum.serializer.BattleSerializer;
import at.technikum.server.request.RequestImpl;
import at.technikum.server.response.ResponseBuilderImpl;
import at.technikum.server.response.ResponseImpl;
import at.technikum.utils.TextColor;
import com.google.gson.JsonObject;

public class BattleControl implements Post {

    private TextColor textColor;
    private PlayerHandler playerHandler;
    private BattleHandler battleHandler;
    private BattleSerializer battleSerializer;
    private LoggerStatic loggerStatic;

    public BattleControl() {
        this.textColor = new TextColor();
        this.playerHandler = new PlayerHandlerImpl();
        this.battleHandler = new BattleHandlerImpl();
        this.battleSerializer = new BattleSerializer();
        this.loggerStatic = LoggerStatic.getInstance();
    }

    /** START - Battle **/
    public ResponseImpl post(RequestImpl requestImpl) {
        //System.out.println("START GAME");
        loggerStatic.log("\nSTART GAME\n");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerHandler.getItemById(requestImpl.getAuth());

        /** --> DECK - EMPTY **/
        if (currentPlayer.getDeck() == null) {
            //System.out.println(TextColor.ANSI_RED + "DECK EMPTY" + TextColor.ANSI_RESET);
            loggerStatic.log("\nDECK EMPTY\n");
            return new ResponseBuilderImpl().statusMethodNotAllowed("DECK EMPTY");
        }

        Battle currentBattle = battleHandler.startBattle(currentPlayer);


        if(currentBattle.getPlayer2() == null){
            loggerStatic.log("\nBATTLE - SEARCHING ...\n");
            return new ResponseBuilderImpl().statusOK(battleSerializer.message("BATTLE - SEARCHING ...").toString());
        }

        currentBattle = battleHandler.playGame(currentBattle);

        JsonObject jsonObject = battleSerializer.convertBattleToJson(currentBattle,false,true,true,false,true,false);
        //System.out.println(this.textColor.ANSI_GREEN + "LOADING FINISHED!" + this.textColor.ANSI_RESET);
        loggerStatic.log("\nGAME ENDE \n");
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }

}
