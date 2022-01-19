package at.technikum.model;

import at.technikum.utils.card.ICard;
import at.technikum.utils.card.cardTypes.CardType;

public interface Trade {

    String getTradeID();

    String getUserID();

    ICard getCard();

    double getMinPower();

    CardType getCardTyp();
}
