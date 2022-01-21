package at.technikum.model.repository;

import at.technikum.model.card.ICard;

import java.util.ArrayList;

public interface Stack {
    String getUserID();

    ArrayList<ICard> getStack();
}
