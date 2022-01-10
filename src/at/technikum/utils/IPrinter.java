package at.technikum.utils;

import at.technikum.model.IDeck;
import at.technikum.model.IPlayer;
import at.technikum.model.IProfil;
import at.technikum.model.IStack;

public interface IPrinter {
    void printStack(IStack currentStack);

    void printPlayerInfo(IProfil playerInfo);

    void printDeck(IDeck currentDeck);

    void printPlayerData(IPlayer currentPlayer);
}
