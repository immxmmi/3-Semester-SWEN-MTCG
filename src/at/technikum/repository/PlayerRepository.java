package at.technikum.repository;

import at.technikum.database.AbstractDBTable;
import at.technikum.model.IProfil;
import at.technikum.model.Profil;
import at.technikum.model.IPlayer;
import at.technikum.model.Player;
import at.technikum.utils.tools.TextColor;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;


public class PlayerRepository extends AbstractDBTable implements IPlayerRepository {

    @Getter
    private static PlayerRepository instance;
    public static IPlayer currentPlayer;
    private String sessionToken = "";


    private ICardHolderRepository cardHolderServices;
    private IProfilRepository playerInfoService;
    private IStackRepository stackService;
    private IDeckRepository deckService;

    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public PlayerRepository() {

        this.tableName = "player";
        this.cardHolderServices = new CardHolderRepository();
        this.playerInfoService = new ProfilRepository();
        this.stackService = new StackRepository();
        this.deckService = new DeckRepository();
    }
    /*******************************************************************/



    /*******************************************************************/
    /**                            Builder                            **/
    /*******************************************************************/
    private IPlayer playerBuilder(ResultSet result) {
        try {
            if (result.next()) {
                IPlayer player = Player.builder()
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
    public IPlayer Login(String username, String password) {

        this.currentPlayer = this.getPlayerByUsername(username);

        if (this.currentPlayer != null) {
            if(authorizePassword(this.currentPlayer.getPassword(),password) == false){
                    System.out.println(TextColor.ANSI_RED + "ERROR - Password is wrong! " + TextColor.ANSI_RESET);
                return null;
            }

            this.currentPlayer.setStatus(startSession(currentPlayer));
            this.currentPlayer.setToken(this.sessionToken);
        }
        return currentPlayer;
    }

    @Override
    public void Logout(IPlayer currentPlayer) {
        if (currentPlayer != null) {
            this.stopSession(currentPlayer);
        }
    }

    @Override
    public IPlayer Register(String id, String username, String password) {
        if (id == "") {
            id = "U-" + this.tokenSupplier.get();
        }
        if (getPlayerByUsername(username) != null) {
            System.out.println(TextColor.ANSI_RED + "Username exist already" + TextColor.ANSI_RESET);
            return null;
        }
        IPlayer newPlayer = Player.builder()
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
    private boolean startSession(IPlayer newPlayer) {
        System.out.println(TextColor.ANSI_GREEN + "#START SESSION:" + TextColor.ANSI_RESET);
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

    private boolean stopSession(IPlayer currentPlayer) {
        System.out.println(TextColor.ANSI_RED + "#STOP SESSION:" + TextColor.ANSI_RESET);
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
    public IPlayer reloadAccount(IPlayer currentPlayer) {
        this.update(currentPlayer);
        return getPlayerById(currentPlayer.getUserID());
    }

    @Override
    public boolean giveCoins(IPlayer currentPlayer, double price) {

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
    public void addCoins(IPlayer currentPlayer, double coins) {
        double newBudge = currentPlayer.getCoins() + coins;
        currentPlayer.setCoins(newBudge);
        this.update(currentPlayer);
        this.reloadAccount(currentPlayer);
    }



    @Override
    public IPlayer loadPlayerStackDeck(IPlayer currentPlayer) {
        currentPlayer.setDeck(this.deckService.getDeckById(currentPlayer.getUserID()));
        currentPlayer.setStack(this.stackService.loadStack(currentPlayer.getUserID()));
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
    public PlayerRepository getInstance() {
        if (PlayerRepository.instance == null) {
            PlayerRepository.instance = new PlayerRepository();
        }
        return PlayerRepository.instance;
    }

    @Override
    public IPlayer getPlayerById(String id) {
        this.parameter = new String[]{id};
        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE user_id = ? " + ";",
                this.parameter
        );
        return playerBuilder(this.result);
    }

    @Override
    public IPlayer getPlayerByUsername(String username) {
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
    public IPlayer insert(IPlayer newPlayer) {
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

        IProfil playerInfo = Profil.builder()
                .name(newPlayer.getUsername())
                .bio("Welcome " + newPlayer.getUsername())
                .image(":)")
                .build();

        //this.playerInfoService.setInfo(playerInfo);
        return getPlayerById(newPlayer.getUserID());
    }

    @Override
    /** UPDATE CURRENT PLAYER **/
    public IPlayer update(IPlayer currentPlayer) {
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

        return getPlayerById(currentPlayer.getUserID());
    }

    @Override
    /** DELETE CURRENT PLAYER **/
    public boolean delete(IPlayer currentPlayer) {
        //System.out.println(ANSI_BLUE + "#DELETE:" + ANSI_RESET);
        this.parameter = new String[]{};
        if (getPlayerById(currentPlayer.getUserID()) == null) {
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
        IPlayer currentPlayer;
        this.parameter = new String[]{};
        if (getPlayerById(userID) == null) {
            return false;
        } else {
            currentPlayer = getPlayerById(userID);
        }

        this.stopSession(currentPlayer);
        this.playerInfoService.delete(playerInfoService.getInfoByID(currentPlayer.getUserID()));
        this.deckService.delete(deckService.getDeckById(currentPlayer.getUserID()));
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
