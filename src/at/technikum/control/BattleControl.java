package at.technikum.control;

import at.technikum.control.repository.Post;
import at.technikum.model.repository.Battle;
import at.technikum.repository.BattleRepository;
import at.technikum.repository.BattleRepositoryImpl;
import at.technikum.serializer.BattleSerializer;
import at.technikum.net.server.utils.request.RequestImpl;
import at.technikum.net.server.utils.response.ResponseImpl;
import at.technikum.net.server.utils.response.ResponseBuilderImpl;
import at.technikum.model.repository.Player;
import at.technikum.repository.PlayerRepository;
import at.technikum.repository.PlayerRepositoryImpl;
import at.technikum.utils.tools.TextColor;
import com.google.gson.JsonObject;

public class BattleControl implements Post {

    TextColor textColor;
    PlayerRepository playerRepository;
    BattleRepository battleRepository;
    BattleSerializer battleSerializer;

    public BattleControl() {
        this.textColor = new TextColor();
        this.playerRepository = new PlayerRepositoryImpl();
        this.battleRepository = new BattleRepositoryImpl();
        this.battleSerializer = new BattleSerializer();
    }

    /** START - Battle **/
    public ResponseImpl post(RequestImpl requestImpl) {

        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerRepository.getPlayerById(requestImpl.getAuth());

        /** --> DECK - EMPTY **/
        if (currentPlayer.getDeck() == null) {
            System.out.println(TextColor.ANSI_RED + "DECK EMPTY" + TextColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed("DECK EMPTY");
        }

        System.out.println("START GAME");
        Battle currentBattle = battleRepository.startBattle(currentPlayer);
        if(currentBattle == null){
            return new ResponseBuilderImpl().statusOK(battleSerializer.message("BATTLE - SEARCHING ...").toString());
        }

        currentBattle = battleRepository.playGame(currentBattle);
        JsonObject jsonObject = battleSerializer.convertBattleToJson(currentBattle,false,false,false,true,true,false);

        System.out.println(this.textColor.ANSI_GREEN + "LOADING FINISHED!" + this.textColor.ANSI_RESET);
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }

}
