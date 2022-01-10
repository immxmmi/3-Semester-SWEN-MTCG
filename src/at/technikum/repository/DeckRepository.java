package at.technikum.repository;

import at.technikum.model.IDeck;

import java.util.ArrayList;

public interface DeckRepository extends Repository<IDeck> {
    boolean setNewDeck(ArrayList<String> newDeck, String holderID);

    DeckRepositoryImpl getInstance();

    IDeck getDeckById(String userID);
}
