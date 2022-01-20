package at.technikum.repository;

import at.technikum.database.AbstractDBTable;
import at.technikum.model.*;
import at.technikum.services.BattleLogic;
import at.technikum.services.BattleLogicImpl;
import at.technikum.utils.card.service.CardServices;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BattleRepositoryImpl extends AbstractDBTable implements BattleRepository {

    private CardServices cardServices = new CardServices();
    private BattleLogic battleLogic = new BattleLogicImpl();
    private PlayerRepository playerRepository = new PlayerRepositoryImpl();
    private CardHolderRepository cardHolderRepository = new CardHolderRepositoryImpl();

    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public BattleRepositoryImpl() {
        this.tableName = "battle";
    }
    /*******************************************************************/



    /*******************************************************************/
    /**                            BUILDER                            **/
    /*******************************************************************/
    private Battle battleBuilder(ResultSet result) {
        try {
            if (result.next()) {
                Player player2 = null;
                if(!result.getString("player2").equals("null")){
                    player2 = playerRepository.getPlayerById(result.getString("player2"));
                }
                Battle battle = BattleImpl.builder()
                        .battleID(result.getString("battle_id"))
                        .player1(playerRepository.getPlayerById(result.getString("player1")))
                        .player2(player2)
                        .winner(playerRepository.getPlayerById(result.getString("winner")))
                        .round(convertToInt(result.getString("round")))
                        .searching(convertToBoolean(result.getString("searching")))
                        .build();
                this.closeStatement();
                return battle;
            }
        } catch (SQLException e) {

            System.out.println("GETOBJECT -ERRROR: " + e);
            e.printStackTrace();
        }
        this.closeStatement();
        return null;
    }
    /*******************************************************************/


    private Battle checkSearching(Player player){
        this.parameter = new String[]{player.getUserID(),"true"};
        this.setStatement("SELECT * FROM " + this.tableName + " WHERE player1 = ? AND searching = ? ;", this.parameter);
        return battleBuilder(this.result);
    }

    @Override
    public Battle startBattle(Player player) {
        if(checkSearching(player) != null){
            System.out.println(ANSI_GREEN + "BATTLE - SEARCHING ..." + ANSI_RESET);
            return null;
        }
        Battle currentBattle = createBattle(player);
        if(currentBattle.isSearching()){
            System.out.println(ANSI_GREEN + "BATTLE - SEARCHING ..." + ANSI_RESET);
            return null;
        }
        return  currentBattle;
    }
    @Override
    public Battle playGame(Battle currentBattle){
        BattleLogic battleLogic = new BattleLogicImpl();
        return updateBattle(battleLogic.start(currentBattle));
    }
    private Battle createBattle(Player player) {
        if(getAllActiveBattle().size() > 0){
            for(int i = 0; i < getAllActiveBattle().size(); i++){
                if(!player.getUserID().equals(getAllActiveBattle().get(i).getPlayer1().getUserID())) {
                    Battle updateBattle =  getAllActiveBattle().get(i);
                    updateBattle.setPlayer2(player);
                    updateBattle.setSearching(false);
                    return updateBattle(updateBattle);
                }
            }
        }

        Battle newBattle = BattleImpl.builder()
                .player1(player)
                .searching(true)
                .build();

        return insertBattle(newBattle);
    }

    /*******************************************************************/
    /**                     Datenbank - Operatoren                    **/
    /*******************************************************************/
    @Override
    public Battle getBattleByID(String battleID){
        this.parameter = new String[]{battleID};
        this.setStatement("SELECT * FROM " + this.tableName + " WHERE battle_id = ? ;", this.parameter);

        return battleBuilder(this.result);
    }
    private ArrayList<Battle> getAllActiveBattle() {
        ArrayList<Battle> battles = new ArrayList<>();
        this.parameter = new String[]{"true"};
        this.setStatement("SELECT * FROM " + this.tableName + " WHERE searching = ? ;", this.parameter);

        try {
            while (this.result.next()) {
                String battleID = result.getString("battle_id");
                Player player1 = playerRepository.getPlayerById(result.getString("player1"));
                Player player2 = null;
                if(!result.getString("player2").equals("null")){
                    player2 = playerRepository.getPlayerById(result.getString("player2"));
                }
                Player winner = playerRepository.getPlayerById(result.getString("winner"));
                int round = convertToInt(result.getString("round"));
                Boolean searching = convertToBoolean(result.getString("searching"));

                Battle battle = BattleImpl.builder()
                        .battleID(battleID)
                        .player1(player1)
                        .player2(player2)
                        .winner(winner)
                        .round(round)
                        .searching(searching)
                        .build();
                battles.add(battle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.closeStatement();
        return battles;
    }
    private Battle insertBattle(Battle newBattle){
        this.parameter = new String[]{
                "B-"+this.tokenSupplier.get(),
                newBattle.getPlayer1().getUserID(),
                "null",
                ""+newBattle.getRound(),
                "noWinner",
                ""+newBattle.isSearching()
        };
        this.setStatement(
                "INSERT INTO "+this.tableName+" (battle_id, player1, player2, round , winner, searching) values ( ?,?,?,?,?,?)",
                this.parameter
        );
        return getBattleByID(newBattle.getBattleID());
    }
    private boolean deleteByID(Battle battle) {
        // System.out.println("#DELETE ITEM");
        this.parameter = new String[]{battle.getBattleID()};
        this.setStatement("DELETE FROM " + this.tableName + " WHERE battle_id = ? ;", this.parameter);
        this.closeStatement();
        return true;
    }
    private Battle updateBattle(Battle newBattle) {
        String winner = "noWinner";
        if(newBattle.getWinner() != null){
            winner = newBattle.getWinner().getUserID();
        }
        this.parameter = new String[]{newBattle.getPlayer2().getUserID(),""+newBattle.getRound(),winner,""+newBattle.isSearching(), newBattle.getBattleID()};
        setStatement("UPDATE  \"" + this.tableName + "\" SET  player2 = ?, round = ?, winner = ?, searching = ? WHERE  battle_id= ?;", this.parameter);
        return  getBattleByID(newBattle.getBattleID());
    }

    /*******************************************************************/


}
