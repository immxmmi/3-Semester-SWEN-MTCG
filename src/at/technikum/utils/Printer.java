package at.technikum.utils;

import at.technikum.model.IDeck;
import at.technikum.model.Player;
import at.technikum.model.Profil;
import at.technikum.model.Stack;

import java.util.LinkedHashMap;

public interface Printer {
    void printStack(Stack currentStack);

    void printHighscoreList(LinkedHashMap<String, Double> highscores);

    void printPlayerInfo(Profil playerInfo);

    void printDeck(IDeck currentDeck);

    void printPlayerData(Player currentPlayer);
}
