package at.technikum.model.repository;

import at.technikum.model.card.Card;

import java.util.ArrayList;

public interface Package {

    String getPackageID();

    ArrayList<Card> getCards();

    int getPrice();

    String getDate();

}
