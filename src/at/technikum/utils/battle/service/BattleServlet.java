package at.technikum.utils.battle.service;

import at.technikum.server.utils.request.RequestImpl;
import at.technikum.server.utils.response.ResponseImpl;
import at.technikum.server.utils.response.ResponseBuilderImpl;
import at.technikum.model.Player;
import at.technikum.repository.PlayerRepository;
import at.technikum.repository.PlayerRepositoryImpl;
import at.technikum.utils.tools.TextColor;

public class BattleServlet {

    PlayerRepository playerService;

    public BattleServlet() {
        this.playerService = new PlayerRepositoryImpl();
    }

    public ResponseImpl POST(RequestImpl requestImpl) {

        System.out.println("# LOAD DECK");

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

        IBattleLogic battleLogic = new BattleLogic();

        battleLogic.start(currentPlayer.getUserID(),currentPlayer.getUserID());

        System.out.println(TextColor.ANSI_GREEN + "LOADING FINISHED!" + TextColor.ANSI_RESET);
        return new ResponseBuilderImpl().statusOK("test");
    }
}
