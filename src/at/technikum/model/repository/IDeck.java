package at.technikum.model.repository;

import at.technikum.model.card.ICard;

import java.util.ArrayList;
import java.util.List;

public interface IDeck {
    String getUserID();

    List<ICard> getDeckList();

    ArrayList<String> getCardIDs();

}
