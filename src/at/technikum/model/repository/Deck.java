package at.technikum.model.repository;

import at.technikum.model.card.ICard;

import java.util.ArrayList;
import java.util.List;

public interface Deck {
    String getUserID();

    List<ICard> getDeckList();

    ArrayList<String> getCardIDs();

}
