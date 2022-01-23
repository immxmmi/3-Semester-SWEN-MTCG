package at.technikum.handler.repository;


import at.technikum.handler.PlayerHandlerImpl;
import at.technikum.model.repository.Player;

import java.util.LinkedHashMap;

public interface PlayerHandler {

    Player login(String username, String password);

    void logout(Player currentPlayer);

    Player register(String id, String username, String password);

    Player reloadAccount(Player currentPlayer);

    LinkedHashMap<String, Double> getHighScoreList();

    boolean giveCoins(Player currentPlayer, double price);

    void addCoins(Player currentPlayer, double coins);

    Player loadPlayerStackDeck(Player currentPlayer);

    boolean delete(Player currentPlayer);

    PlayerHandlerImpl getInstance();

    Player getItemById(String id);

    Player getPlayerByUsername(String username);

    Player update(Player currentPlayer);
}
