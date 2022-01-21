package at.technikum.utils;

import at.technikum.model.repository.Deck;
import at.technikum.model.repository.Player;
import at.technikum.model.repository.Profil;
import at.technikum.model.repository.Stack;

import java.util.LinkedHashMap;

public interface Printer {
    void printStack(Stack currentStack);

    void printHighscoreList(LinkedHashMap<String, Double> highscores);

    void printPlayerInfo(Profil playerInfo);

    void printDeck(Deck currentDeck);

    void printPlayerData(Player currentPlayer);
}
