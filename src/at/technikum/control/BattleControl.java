package at.technikum.control;

import at.technikum.model.Battle;
import at.technikum.model.BattleImpl;
import at.technikum.repository.BattleRepository;
import at.technikum.repository.BattleRepositoryImpl;
import at.technikum.serializer.BattleSerializer;
import at.technikum.server.utils.request.RequestImpl;
import at.technikum.server.utils.response.ResponseImpl;
import at.technikum.server.utils.response.ResponseBuilderImpl;
import at.technikum.model.Player;
import at.technikum.repository.PlayerRepository;
import at.technikum.repository.PlayerRepositoryImpl;
import at.technikum.utils.tools.TextColor;
import com.google.gson.JsonObject;

public class BattleControl {

    PlayerRepository playerService;
    BattleRepository battleRepository;
    BattleSerializer battleSerializer;

    public BattleControl() {

        this.playerService = new PlayerRepositoryImpl();
        this.battleRepository = new BattleRepositoryImpl();
        this.battleSerializer = new BattleSerializer();
        this.battleSerializer = new BattleSerializer();
    }

    public ResponseImpl POST(RequestImpl requestImpl) throws InterruptedException {

        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerService.getPlayerById(requestImpl.getAuth());

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(TextColor.ANSI_RED + "USER NOT FOUND" + TextColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed("USER NOT FOUND");
        }
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

        System.out.println(TextColor.ANSI_GREEN + "LOADING FINISHED!" + TextColor.ANSI_RESET);
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }









}
