package at.technikum.handler.repository;

import at.technikum.handler.DeckHandlerImpl;
import at.technikum.model.repository.IDeck;

import java.util.ArrayList;

public interface DeckHandler extends Repository<IDeck> {
    boolean setNewDeck(ArrayList<String> newDeck, String holderID);

    DeckHandlerImpl getInstance();

    IDeck getItemById(String userID);
}
