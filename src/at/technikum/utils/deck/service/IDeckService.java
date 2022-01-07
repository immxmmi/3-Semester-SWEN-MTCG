package at.technikum.utils.deck.service;

import at.technikum.database.IDBTable;
import at.technikum.utils.deck.IDeck;

import java.util.ArrayList;

public interface IDeckService extends IDBTable<IDeck> {
    boolean setNewDeck(ArrayList<String> newDeck, String holderID);

    DeckService getInstance();


    void printDeck(IDeck currentDeck);

    IDeck getDeckById(String userID);
}
