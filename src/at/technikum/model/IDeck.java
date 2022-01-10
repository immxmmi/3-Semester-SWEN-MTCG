package at.technikum.model;

import at.technikum.utils.card.ICard;

import java.util.ArrayList;
import java.util.List;

public interface IDeck {
    String getUserID();

    List<ICard> getDeckList();

    ArrayList<String> getCardIDs();

}
