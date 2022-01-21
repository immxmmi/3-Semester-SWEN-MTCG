package at.technikum.repository;


import at.technikum.model.repository.Player;
import at.technikum.repository.util.Repository;

import java.util.LinkedHashMap;

public interface PlayerRepository extends Repository<Player> {

    Player Login(String username, String password);

    void Logout(Player currentPlayer);

    Player Register(String id, String username, String password);

    Player reloadAccount(Player currentPlayer);

    LinkedHashMap<String, Double> getHighScoreList();

    boolean giveCoins(Player currentPlayer, double price);

    void addCoins(Player currentPlayer, double coins);

    Player loadPlayerStackDeck(Player currentPlayer);

    boolean addCardToUser(String userID, String cardID);

    PlayerRepositoryImpl getInstance();

    Player getPlayerById(String id);

    Player getPlayerByUsername(String username);

    boolean deleteByID(String userID);
}
