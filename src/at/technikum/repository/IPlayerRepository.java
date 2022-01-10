package at.technikum.repository;


import at.technikum.database.IDBTable;
import at.technikum.model.IPlayer;

public interface IPlayerRepository extends IDBTable<IPlayer> {


    IPlayer Login(String username, String password);

    void Logout(IPlayer currentPlayer);

    IPlayer Register(String id, String username, String password);

    IPlayer reloadAccount(IPlayer currentPlayer);

    boolean giveCoins(IPlayer currentPlayer, double price);

    void addCoins(IPlayer currentPlayer, double coins);

    IPlayer loadPlayerStackDeck(IPlayer currentPlayer);

    boolean addCardToUser(String userID, String cardID);

    PlayerRepository getInstance();

    IPlayer getPlayerById(String id);

    IPlayer getPlayerByUsername(String username);

    boolean deleteByID(String userID);
}
