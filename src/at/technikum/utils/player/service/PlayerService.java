package at.technikum.utils.player.service;

import at.technikum.database.AbstractDBTable;
import at.technikum.utils.cardHolder.service.CardHolderServices;
import at.technikum.utils.cardHolder.service.ICardHolderServices;
import at.technikum.utils.deck.IDeck;
import at.technikum.utils.deck.service.DeckService;
import at.technikum.utils.deck.service.IDeckService;
import at.technikum.utils.player.IPlayer;
import at.technikum.utils.player.Player;
import at.technikum.utils.profil.IProfil;
import at.technikum.utils.profil.Profil;
import at.technikum.utils.profil.service.IProfilService;
import at.technikum.utils.profil.service.ProfilService;
import at.technikum.utils.stack.IStack;
import at.technikum.utils.stack.service.IStackService;
import at.technikum.utils.stack.service.StackService;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;


public class PlayerService extends AbstractDBTable implements IPlayerService {

    @Getter
    private static PlayerService instance;
    public static IPlayer currentPlayer;
    private String sessionToken = "";


    private ICardHolderServices cardHolderServices;
    private IProfilService playerInfoService;
    private IStackService stackService;
    private IDeckService deckService;

    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public PlayerService() {

        this.tableName = "player";
        this.cardHolderServices = new CardHolderServices();
        this.playerInfoService = new ProfilService();
        this.stackService = new StackService();
        this.deckService = new DeckService();
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

            System.out.println(ANSI_RED + "GETOBJECT -ERRROR: " + e + ANSI_RESET);
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
        String[] logindata = new String[]{username, password};
        this.currentPlayer = this.getPlayerByLoginData(logindata);
        if (this.currentPlayer != null) {
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
        if (this.currentPlayer != null) {
            stopSession(currentPlayer);
        }

        if (getPlayerByUsername(username) != null) {
            System.out.println(ANSI_RED + "Username exist already" + ANSI_RESET);
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
        System.out.println(ANSI_GREEN + "#START SESSION:" + ANSI_RESET);
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
        System.out.println(ANSI_RED + "#STOP SESSION:" + ANSI_RESET);
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
    public void printPlayerData(IPlayer currentPlayer) {
        int maxName = 27;
        int maxCoins = 30;
        int maxElo = 32;
        int maxStackSize = 25;
        System.out.println("###############################################");
        System.out.println("#                  USER DATA                  #");
        System.out.println("###############################################");
        System.out.println("#                                             #");
        System.out.println("#       Username: " + ANSI_BLUE + currentPlayer.getUsername() + checkSpace(currentPlayer.getUsername(), maxName) + ANSI_RESET + "#");
        System.out.println("#       Coins: " + ANSI_BLUE + currentPlayer.getCoins() + checkSpace("" + currentPlayer.getCoins(), maxCoins) + ANSI_RESET + "#");
        System.out.println("#       ELO: " + ANSI_BLUE + currentPlayer.getElo() + checkSpace("" + currentPlayer.getElo(), maxElo) + ANSI_RESET + "#");
        System.out.println("#       Stack Size: " + ANSI_BLUE + currentPlayer.getStack().getStack().size() + checkSpace("" + currentPlayer.getStack().getStack().size(), maxStackSize) + ANSI_RESET + "#");
        System.out.println("#                                             #");
        System.out.println("###############################################");
    }

    @Override
    public IPlayer loadPlayerStackDeck(IPlayer currentPlayer) {
        IDeck deck = this.deckService.getDeckById(currentPlayer.getUserID());
        IStack stack = this.stackService.loadStack(currentPlayer.getUserID());
        IStack freeStack = this.stackService.loadFreeStack(currentPlayer.getUserID());
        currentPlayer.setStackDeck(stack, freeStack, deck);
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
    public PlayerService getInstance() {
        if (PlayerService.instance == null) {
            PlayerService.instance = new PlayerService();
        }
        return PlayerService.instance;
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

    private IPlayer getPlayerByLoginData(String[] parameterLogin) {
        String username = parameterLogin[0];
        String password = parameterLogin[1];

        if (getPlayerByUsername(username) == null) {
            return null;
        }
        if (getPlayerByUsername(username).authorizePassword(password) == false) {
            System.out.println(ANSI_RED + "ERROR - Password is wrong! " + ANSI_RESET);
            return null;
        }
        ;


        this.parameter = new String[]{username, hashString(password)};
        setStatement("SELECT * FROM player WHERE username=? AND password=?", this.parameter);

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

        this.playerInfoService.setInfo(playerInfo);
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
        /*
    }

    @Override
    public boolean deleteByUsername(String username) {
        System.out.println("#DELETE:");
        IPlayer currentPlayer;


        if (getPlayerByUsername(username) == null) {
            return false;
        } else {
            currentPlayer = getPlayerByUsername(username);
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
        /*

        Scanner myObj = new Scanner(System.in);
        System.out.println("Are you sure you want to delete (Y/N)?");
        System.out.println("ID: '" + currentPlayer.getUserID() + "'");
        System.out.println("User: '" + currentPlayer.getUsername() + "'");
        String input = myObj.nextLine();
        if (input.equals("y") || input.equals("Y")) {

            this.setStatement("DELETE FROM " + this.tableName +
                            " WHERE user_id = '" + currentPlayer.getUserID() + "';"
                    , this.parameter);
        } else {
            return false;
        }
        return true;
    }

         */
    }
    /*******************************************************************/
}
