package at.technikum.utils.battle.service;

import at.technikum.server.repository.Repository;
import at.technikum.server.request.Request;
import at.technikum.server.response.Response;
import at.technikum.utils.player.IPlayer;
import at.technikum.utils.player.service.IPlayerService;
import at.technikum.utils.player.service.PlayerService;

public class BattleServlet extends Repository {

    IPlayerService playerService;

    public BattleServlet() {
        this.playerService = new PlayerService();
    }

    @Override
    public Response POST(Request request) {

        System.out.println("# LOAD DECK");

        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST **/
        Response response = new Response().requestErrorHandler(request, true, false);
        if (response != null) {
            return response;
        }

        /** --> INSTANCE **/
        IPlayer currentPlayer = this.playerService.getPlayerById(request.getAuth());

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "USER NOT FOUND" + ANSI_RESET);
            return new Response().statusMethodNotAllowed();
        }
        if (currentPlayer.getDeck() == null) {
            System.out.println(ANSI_RED + "DECK EMPTY" + ANSI_RESET);
            return new Response().statusMethodNotAllowed();
        }

        System.out.println("START GAME");

        IBattleLogic battleLogic = new BattleLogic();

        battleLogic.start(currentPlayer.getUserID(),currentPlayer.getUserID());

        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);
        return new Response().statusOK();
    }
}
