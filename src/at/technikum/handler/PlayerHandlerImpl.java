package at.technikum.handler;

import at.technikum.database.AbstractDBTable;
import at.technikum.handler.repository.*;
import at.technikum.logger.LoggerStatic;
import at.technikum.model.repository.Player;
import at.technikum.model.PlayerImpl;
import at.technikum.model.repository.Profil;
import at.technikum.model.ProfilImpl;
import at.technikum.utils.TextColor;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class PlayerHandlerImpl extends AbstractDBTable implements PlayerHandler {

    @Getter
    private static PlayerHandlerImpl instance;
    public static Player currentPlayer;
    private String sessionToken = "";

    private CardHolderHandler cardHolderServices;
    private ProfilHandler playerInfoService;
    private StackHandler stackService;
    private DeckHandler deckService;
    private LoggerStatic loggerStatic;

    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public PlayerHandlerImpl() {

        this.tableName = "player";
        this.cardHolderServices = new CardHolderHandlerImpl();
        this.playerInfoService = new ProfilHandlerImpl();
        this.stackService = new StackHandlerImpl();
        this.deckService = new DeckHandlerImpl();
        this.loggerStatic = LoggerStatic.getInstance();
    }
    /*******************************************************************/



    /*******************************************************************/
    /**                            Builder                            **/
    /*******************************************************************/
    private Player playerBuilder(ResultSet result) {
        try {
            if (result.next()) {
                Player player = PlayerImpl.builder()
                        .userID(result.getString("user_id"))
                        .username(result.getString("username"))
                        .password(result.getString("password"))
                        .coins(convertToDouble(result.getString("user_coins")))
                        .elo(convertToDouble(result.getString("user_elo")))
                        .status(convertToBoolean(result.getString("user_online")))
                        .build();
                player = loadPlayerStackDeck(player);

                this.closeStatement();

                return player;
            }
        } catch (SQLException e) {

            System.out.println(TextColor.ANSI_RED + "GETOBJECT -ERRROR: " + e + TextColor.ANSI_RESET);
            e.printStackTrace();
        }
        this.closeStatement();
        return null;
    }
    /*******************************************************************/

    /*******************************************************************/
    /**                        LOGIN/REGISTER                         **/
    /*******************************************************************/
    @Override
    public Player Login(String username, String password) {

        this.currentPlayer = this.getPlayerByUsername(username);

        if (this.currentPlayer != null) {
            if(authorizePassword(this.currentPlayer.getPassword(),password) == false){
                loggerStatic.log("ERROR - Password is wrong! ");
                   // System.out.println(TextColor.ANSI_RED + "ERROR - Password is wrong! " + TextColor.ANSI_RESET);
                return null;
            }

            this.currentPlayer.setStatus(startSession(currentPlayer));
            this.currentPlayer.setToken(this.sessionToken);
        }
        return currentPlayer;
    }

    @Override
    public void Logout(Player currentPlayer) {
        if (currentPlayer != null) {
            this.stopSession(currentPlayer);
        }
    }

    @Override
    public Player Register(String id, String username, String password) {
        if (id == "") {
            id = "U-" + this.tokenSupplier.get();
        }
        if (getPlayerByUsername(username) != null) {
            loggerStatic.log("Username exist already");
            //System.out.println(TextColor.ANSI_RED + "Username exist already" + TextColor.ANSI_RESET);
            return null;
        }
        Player newPlayer = PlayerImpl.builder()
                .userID(id)
                .username(username)
                .password(hashString(password))
                .build();
        this.currentPlayer = this.insert(newPlayer);
        return this.currentPlayer;
    }

    /**
     * --> SESSION
     **/
    private boolean startSession(Player newPlayer) {
       // System.out.println(TextColor.ANSI_GREEN + "#START SESSION:" + TextColor.ANSI_RESET);
        if (newPlayer == null) {
            return false;
        }
        this.sessionToken = ("ON-" + this.tokenSupplier.get());

        this.parameter = new String[]{
                "" + this.sessionToken,
                "" + newPlayer.getUserID(),
                "" + formatDate(0)
        };

        this.setStatement("INSERT INTO session (session_id, user_id,date)VALUES(?,?,?);", this.parameter);
        newPlayer.setStatus(true);
        update(newPlayer);
        return true;
    }

    private boolean stopSession(Player currentPlayer) {
     //   System.out.println(TextColor.ANSI_RED + "#STOP SESSION:" + TextColor.ANSI_RESET);
        this.parameter = new String[]{};
        if (currentPlayer == null) {
            //    System.out.println(ANSI_RED +"ERROR - NO SESSION" + ANSI_RESET);
            return false;
        }
        this.setStatement("DELETE FROM session" +
                        " WHERE user_id = '" + currentPlayer.getUserID() + "';"
                , this.parameter);

        currentPlayer.setStatus(false);
        update(currentPlayer);
        return true;
    }

    /*******************************************************************/


    /*******************************************************************/
    /**                            METHODE                            **/
    /*******************************************************************/

    /**
     * -> läd den Account neu
     **/
    @Override
    public Player reloadAccount(Player currentPlayer) {
        this.update(currentPlayer);
        return this.getItemById(currentPlayer.getUserID());
    }

    @Override
    public LinkedHashMap<String, Double> getHighScoreList(){
        HashMap<String,Double> highscore = new HashMap<> ();

        this.parameter = new String[]{};
        this.setStatement("SELECT * FROM " + this.tableName + " ORDER BY user_elo DESC;", this.parameter);

        try {
            while (this.result.next()) {
                Player currentPlayer = PlayerImpl.builder()
                        .userID(result.getString("user_id"))
                        .username(result.getString("username"))
                        .password(result.getString("password"))
                        .coins(convertToDouble(result.getString("user_coins")))
                        .elo(convertToDouble(result.getString("user_elo")))
                        .status(convertToBoolean(result.getString("user_online")))
                        .build();
                highscore.put(currentPlayer.getUsername(),currentPlayer.getElo());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.closeStatement();

        Set<Map.Entry<String, Double>> entries = highscore.entrySet();
        Comparator<Map.Entry<String, Double>> valueComparator = new Comparator<Map.Entry<String,Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> e1, Map.Entry<String, Double> e2) {
                Double v1 = e1.getValue();
                Double v2 = e2.getValue();
                return v1.compareTo(v2);
            }
        };

        List<Map.Entry<String, Double>> listOfEntries = new ArrayList<Map.Entry<String, Double>>(entries);
        Collections.sort(listOfEntries, valueComparator);
        LinkedHashMap<String, Double> sortedByValue = new LinkedHashMap<String, Double>(listOfEntries.size());

        for(Map.Entry<String, Double> entry : listOfEntries){
            sortedByValue.put(entry.getKey(), entry.getValue());
        }


        return sortedByValue;
    }

    @Override
    public boolean giveCoins(Player currentPlayer, double price) {

        double coins = currentPlayer.getCoins();
        if (coins < price) {
            return false;
        }
        coins -= price;
        currentPlayer.setCoins(coins);
        this.update(currentPlayer);
        this.reloadAccount(currentPlayer);
        return true;
    }
    @Override
    public void addCoins(Player currentPlayer, double coins) {
        double newBudge = currentPlayer.getCoins() + coins;
        currentPlayer.setCoins(newBudge);
        this.update(currentPlayer);
        this.reloadAccount(currentPlayer);
    }
    @Override
    public Player loadPlayerStackDeck(Player currentPlayer) {
        currentPlayer.setDeck(this.deckService.getItemById(currentPlayer.getUserID()));
        currentPlayer.setStack(this.stackService.getItemById(currentPlayer.getUserID()));
        currentPlayer.setFreeStack(this.stackService.loadFreeStack(currentPlayer.getUserID()));
        return currentPlayer;
    }


    /*******************************************************************/





    /*******************************************************************/
    /**                             ADD                               **/
    /*******************************************************************/
    @Override
    public boolean addCardToUser(String userID, String cardID) {
        System.out.println("#INSERT CARD");
        String cardHolderID = this.tokenSupplier.get();
        this.parameter = new String[]{cardHolderID, userID, cardID, "false", "false", "0", "0", "1"};
        this.setStatement("INSERT INTO \"cardHolder\" (\"cardHolder_id\", user_id, card_id, user_stack, user_deck, number_stack, number_deck, number) VALUES(?,?,?,?,?,?,?,?);", this.parameter);
        return true;
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                             GETTER                            **/
    /*******************************************************************/
    @Override
    public PlayerHandlerImpl getInstance() {
        if (PlayerHandlerImpl.instance == null) {
            PlayerHandlerImpl.instance = new PlayerHandlerImpl();
        }
        return PlayerHandlerImpl.instance;
    }

    @Override
    public Player getItemById(String id) {
        this.parameter = new String[]{id};
        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE user_id = ? " + ";",
                this.parameter
        );
        return playerBuilder(this.result);
    }

    @Override
    public Player getPlayerByUsername(String username) {
        this.parameter = new String[]{username};
        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE username = ? " + ";",
                this.parameter
        );
        return playerBuilder(this.result);
    }


    /*******************************************************************/

    /*******************************************************************/
    /**                     Datenbank - Operatoren                    **/
    /*******************************************************************/

    @Override
    /** INSERT NEW PLAYER **/
    public Player insert(Player newPlayer) {
        //System.out.println(ANSI_BLUE + "#INSERT:" + ANSI_RESET);
        if (newPlayer == null) {
            return null;
        }
        this.parameter = new String[]{
                "" + newPlayer.getUserID(),
                "" + newPlayer.getUsername(),
                "" + newPlayer.getPassword(),
                "" + newPlayer.getCoins(),
                "" + newPlayer.getElo(),
                "" + newPlayer.isStatus(),
        };
        this.setStatement("INSERT INTO " + this.tableName + "(user_id,username,password,user_coins,user_elo,user_online)VALUES(?,?,?,?,?,?);", this.parameter);

        Profil playerInfo = ProfilImpl.builder()
                .name(newPlayer.getUsername())
                .bio("Welcome " + newPlayer.getUsername())
                .image(":)")
                .build();

        //this.playerInfoService.setInfo(playerInfo);
        return this.getItemById(newPlayer.getUserID());
    }

    @Override
    /** UPDATE CURRENT PLAYER **/
    public Player update(Player currentPlayer) {
        //System.out.println(ANSI_BLUE + "#UPDATE:" + ANSI_RESET);
        if (currentPlayer == null) {
            return null;
        }
        this.parameter = new String[]{
                "" + currentPlayer.getUsername(),
                "" + currentPlayer.getPassword(),
                "" + currentPlayer.getCoins(),
                "" + currentPlayer.getElo(),
                "" + currentPlayer.isStatus(),

        };


        this.setStatement(
                "UPDATE " + this.tableName +
                        " SET username = ?, password = ?, user_coins = ?,user_elo = ?, user_online = ? " +
                        "WHERE user_id = '" + currentPlayer.getUserID() + "' ;"
                , this.parameter
        );

        return this.getItemById(currentPlayer.getUserID());
    }

    @Override
    /** DELETE CURRENT PLAYER **/
    public boolean delete(Player currentPlayer) {
        //System.out.println(ANSI_BLUE + "#DELETE:" + ANSI_RESET);
        this.parameter = new String[]{};
        if (this.getItemById(currentPlayer.getUserID()) == null) {
            return false;
        }
        stopSession(currentPlayer);
        this.setStatement("DELETE FROM " + this.tableName +
                        " WHERE user_id = '" + currentPlayer.getUserID() + "';"
                , this.parameter);
        return true;
    }

    @Override
    public boolean deleteByID(String userID) {
        // System.out.println("#DELETE:");
        Player currentPlayer;
        this.parameter = new String[]{};
        if (this.getItemById(userID) == null) {
            return false;
        } else {
            currentPlayer = this.getItemById(userID);
        }

        this.stopSession(currentPlayer);
        this.playerInfoService.delete(playerInfoService.getItemById(currentPlayer.getUserID()));
        this.deckService.delete(deckService.getItemById(currentPlayer.getUserID()));
        this.cardHolderServices.deleteAllUserCards(currentPlayer);


        this.parameter = new String[]{};
        this.setStatement("DELETE FROM " + this.tableName +
                        " WHERE user_id = '" + currentPlayer.getUserID() + "';"
                , this.parameter);


        return true;
    }
    /*******************************************************************/


    /** AUTH-PW **/
    public boolean authorizePassword(String hashedPassword, String password) {
        return hashedPassword.equals(hashString(password));
    }

}
