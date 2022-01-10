package at.technikum.repository;

import at.technikum.database.IDBTable;
import at.technikum.model.IDeck;

import java.util.ArrayList;

public interface IDeckRepository extends IDBTable<IDeck> {
    boolean setNewDeck(ArrayList<String> newDeck, String holderID);

    DeckRepository getInstance();

    IDeck getDeckById(String userID);
}
