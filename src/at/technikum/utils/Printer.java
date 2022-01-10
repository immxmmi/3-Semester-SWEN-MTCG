package at.technikum.utils;

import at.technikum.model.IDeck;
import at.technikum.model.Player;
import at.technikum.model.Profil;
import at.technikum.model.Stack;

public interface Printer {
    void printStack(Stack currentStack);

    void printPlayerInfo(Profil playerInfo);

    void printDeck(IDeck currentDeck);

    void printPlayerData(Player currentPlayer);
}
