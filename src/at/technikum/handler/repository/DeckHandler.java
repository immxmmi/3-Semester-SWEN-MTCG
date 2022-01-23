package at.technikum.handler.repository;

import at.technikum.handler.DeckHandlerImpl;
import at.technikum.model.repository.Deck;

import java.util.ArrayList;

// TODO: 23.01.2022 Fertig
public interface DeckHandler {
    boolean setNewDeck(ArrayList<String> newDeck, String holderID);

    DeckHandlerImpl getInstance();

    Deck getItemById(String userID);

    boolean delete(Deck item);
}
