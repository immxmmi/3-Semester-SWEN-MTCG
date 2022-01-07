package at.technikum.utils.stack;

import at.mtcg.utils.card.ICard;

import java.util.ArrayList;

public interface IStack {
    String getUserID();

    ArrayList<ICard> getStack();
}
