package at.technikum.repository;

import at.technikum.model.repository.Battle;
import at.technikum.model.repository.Player;
import at.technikum.net.database.AbstractDBTable;
import at.technikum.model.*;
import at.technikum.repository.util.Repository;
import at.technikum.services.BattleLogic;
import at.technikum.services.BattleLogicImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BattleRepositoryImpl extends AbstractDBTable implements BattleRepository, Repository<Battle> {


    private PlayerRepository playerRepository = new PlayerRepositoryImpl();

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

    /*******************************************************************/
    /**                            ACTION                             **/
    /*******************************************************************/

    @Override
    public Battle startBattle(Player player) {
        if(checkSearching(player) != null){
            System.out.println(ANSI_GREEN + "BATTLE - SEARCHING ..." + ANSI_RESET);
            return null;
        }
        Battle currentBattle = createBattle(player);

        return  currentBattle;
    }
    @Override
    public Battle playGame(Battle currentBattle){
        BattleLogic battleLogic = new BattleLogicImpl();
        return update(battleLogic.start(currentBattle));
    }
    private Battle createBattle(Player player) {
        if(getAllActiveBattle().size() > 0){
            for(int i = 0; i < getAllActiveBattle().size(); i++){
                if(!player.getUserID().equals(getAllActiveBattle().get(i).getPlayer1().getUserID())) {
                    Battle updateBattle =  getAllActiveBattle().get(i);
                    updateBattle.setPlayer2(player);
                    updateBattle.setSearching(false);
                    return update(updateBattle);
                }
            }
        }

        Battle newBattle = BattleImpl.builder()
                .player1(player)
                .searching(true)
                .build();

        return insert(newBattle);
    }
    private Battle checkSearching(Player player){
        this.parameter = new String[]{player.getUserID(),"true"};
        this.setStatement("SELECT * FROM " + this.tableName + " WHERE player1 = ? AND searching = ? ;", this.parameter);
        return battleBuilder(this.result);
    }

    /*******************************************************************/

    /*******************************************************************/
    /**                     Datenbank - Operatoren                    **/
    /*******************************************************************/

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
    @Override
    public Battle getItemById(String battleID){
        this.parameter = new String[]{battleID};
        this.setStatement("SELECT * FROM " + this.tableName + " WHERE battle_id = ? ;", this.parameter);

        return battleBuilder(this.result);
    }
    @Override
    public Battle insert(Battle newBattle){
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
        return this.getItemById(newBattle.getBattleID());
    }
    @Override
    public Battle update(Battle newBattle) {
        String winner = "noWinner";
        if(newBattle.getWinner() != null){
            winner = newBattle.getWinner().getUserID();
        }
        this.parameter = new String[]{newBattle.getPlayer2().getUserID(),""+newBattle.getRound(),winner,""+newBattle.isSearching(), newBattle.getBattleID()};
        setStatement("UPDATE  \"" + this.tableName + "\" SET  player2 = ?, round = ?, winner = ?, searching = ? WHERE  battle_id= ?;", this.parameter);
        return  this.getItemById(newBattle.getBattleID());
    }
    @Override
    public boolean delete(Battle battle) {
        // System.out.println("#DELETE ITEM");
        this.parameter = new String[]{battle.getBattleID()};
        this.setStatement("DELETE FROM " + this.tableName + " WHERE battle_id = ? ;", this.parameter);
        this.closeStatement();
        return true;
    }

    /*******************************************************************/


}
