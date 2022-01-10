package at.technikum.utils.battle.service;

import at.technikum.server.utils.request.Request;
import at.technikum.server.utils.response.Response;
import at.technikum.server.utils.response.ResponseBuilder;
import at.technikum.model.Player;
import at.technikum.repository.PlayerRepository;
import at.technikum.repository.PlayerRepositoryImpl;
import at.technikum.utils.tools.TextColor;

public class BattleServlet {

    PlayerRepository playerService;

    public BattleServlet() {
        this.playerService = new PlayerRepositoryImpl();
    }

    public Response POST(Request request) {

        System.out.println("# LOAD DECK");

        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST **/
        Response response = new ResponseBuilder().requestErrorHandler(request, true, false);
        if (response != null) {
            return response;
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerService.getPlayerById(request.getAuth());

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(TextColor.ANSI_RED + "USER NOT FOUND" + TextColor.ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed("USER NOT FOUND");
        }
        if (currentPlayer.getDeck() == null) {
            System.out.println(TextColor.ANSI_RED + "DECK EMPTY" + TextColor.ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed("DECK EMPTY");
        }

        System.out.println("START GAME");

        IBattleLogic battleLogic = new BattleLogic();

        battleLogic.start(currentPlayer.getUserID(),currentPlayer.getUserID());

        System.out.println(TextColor.ANSI_GREEN + "LOADING FINISHED!" + TextColor.ANSI_RESET);
        return new ResponseBuilder().statusOK("test");
    }
}
