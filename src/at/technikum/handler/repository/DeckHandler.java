package at.technikum.handler.repository;

import at.technikum.handler.DeckHandlerImpl;
import at.technikum.model.repository.Deck;

import java.util.ArrayList;

public interface DeckHandler extends Repository<Deck> {
    boolean setNewDeck(ArrayList<String> newDeck, String holderID);

    DeckHandlerImpl getInstance();

    Deck getItemById(String userID);
}
