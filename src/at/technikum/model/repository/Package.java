package at.technikum.model.repository;

import at.technikum.model.card.ICard;

import java.util.ArrayList;

public interface Package {

    String getPackageID();

    ArrayList<ICard> getCards();

    int getPrice();

    String getDate();

}
