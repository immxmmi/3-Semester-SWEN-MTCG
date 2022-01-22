package at.technikum.model.repository;

import at.technikum.model.card.Card;

import java.util.ArrayList;
import java.util.List;

public interface Deck {
    String getUserID();

    List<Card> getDeckList();

    ArrayList<String> getCardIDs();

}
