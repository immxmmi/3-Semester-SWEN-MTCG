package at.technikum.control;

import at.technikum.control.repository.Post;
import at.technikum.logger.LoggerStatic;
import at.technikum.model.repository.Battle;
import at.technikum.repository.BattleRepository;
import at.technikum.repository.BattleRepositoryImpl;
import at.technikum.serializer.BattleSerializer;
import at.technikum.server.utils.request.RequestImpl;
import at.technikum.server.utils.response.ResponseImpl;
import at.technikum.server.utils.response.ResponseBuilderImpl;
import at.technikum.model.repository.Player;
import at.technikum.repository.PlayerRepository;
import at.technikum.repository.PlayerRepositoryImpl;
import at.technikum.utils.TextColor;
import com.google.gson.JsonObject;

public class BattleControl implements Post {

    private TextColor textColor;
    private PlayerRepository playerRepository;
    private BattleRepository battleRepository;
    private BattleSerializer battleSerializer;
    private LoggerStatic loggerStatic;

    public BattleControl() {
        this.textColor = new TextColor();
        this.playerRepository = new PlayerRepositoryImpl();
        this.battleRepository = new BattleRepositoryImpl();
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
        Player currentPlayer = this.playerRepository.getItemById(requestImpl.getAuth());

        /** --> DECK - EMPTY **/
        if (currentPlayer.getDeck() == null) {
            //System.out.println(TextColor.ANSI_RED + "DECK EMPTY" + TextColor.ANSI_RESET);
            loggerStatic.log("\nDECK EMPTY\n");
            return new ResponseBuilderImpl().statusMethodNotAllowed("DECK EMPTY");
        }

        Battle currentBattle = battleRepository.startBattle(currentPlayer);
        if(currentBattle == null){
            loggerStatic.log("\nBATTLE - SEARCHING ...\n");
            return new ResponseBuilderImpl().statusOK(battleSerializer.message("BATTLE - SEARCHING ...").toString());
        }

        currentBattle = battleRepository.playGame(currentBattle);
        JsonObject jsonObject = battleSerializer.convertBattleToJson(currentBattle,false,false,false,true,true,false);

        //System.out.println(this.textColor.ANSI_GREEN + "LOADING FINISHED!" + this.textColor.ANSI_RESET);
        loggerStatic.log("\nLOADING FINISHED!\n");
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }

}
