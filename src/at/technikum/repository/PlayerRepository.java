package at.technikum.repository;


import at.technikum.database.DBTable;
import at.technikum.model.Player;

public interface PlayerRepository extends DBTable<Player> {


    Player Login(String username, String password);

    void Logout(Player currentPlayer);

    Player Register(String id, String username, String password);

    Player reloadAccount(Player currentPlayer);

    boolean giveCoins(Player currentPlayer, double price);

    void addCoins(Player currentPlayer, double coins);

    Player loadPlayerStackDeck(Player currentPlayer);

    boolean addCardToUser(String userID, String cardID);

    PlayerRepositoryImpl getInstance();

    Player getPlayerById(String id);

    Player getPlayerByUsername(String username);

    boolean deleteByID(String userID);
}
