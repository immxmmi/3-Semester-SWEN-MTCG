package at.technikum.model.repository;

import at.technikum.model.card.Card;

import java.util.ArrayList;

public interface Stack {
    String getUserID();

    ArrayList<Card> getStack();
}
