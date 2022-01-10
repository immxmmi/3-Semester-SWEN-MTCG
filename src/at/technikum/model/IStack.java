package at.technikum.model;

import at.technikum.utils.card.ICard;

import java.util.ArrayList;

public interface IStack {
    String getUserID();

    ArrayList<ICard> getStack();
}
