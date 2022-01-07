package at.technikum.utils.stack;

import at.technikum.utils.card.ICard;

import java.util.ArrayList;

public interface IStack {
    String getUserID();

    ArrayList<ICard> getStack();
}
