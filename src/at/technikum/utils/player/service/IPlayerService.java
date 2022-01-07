package at.technikum.utils.player.service;


import at.technikum.database.IDBTable;
import at.technikum.utils.player.IPlayer;

public interface IPlayerService extends IDBTable<IPlayer> {


    IPlayer Login(String username, String password);

    void Logout(IPlayer currentPlayer);

    IPlayer Register(String id, String username, String password);

    IPlayer reloadAccount(IPlayer currentPlayer);

    boolean giveCoins(IPlayer currentPlayer, double price);

    void addCoins(IPlayer currentPlayer, double coins);

    void printPlayerData(IPlayer currentPlayer);

    IPlayer loadPlayerStackDeck(IPlayer currentPlayer);

    boolean addCardToUser(String userID, String cardID);

    PlayerService getInstance();

    IPlayer getPlayerById(String id);

    IPlayer getPlayerByUsername(String username);

    boolean deleteByID(String userID);
}
