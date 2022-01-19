package at.technikum.model;

import at.technikum.utils.card.ICard;

import java.util.ArrayList;

public interface Package {

    String getPackageID();

    ArrayList<ICard> getCards();

    int getPrice();

    String getDate();

}