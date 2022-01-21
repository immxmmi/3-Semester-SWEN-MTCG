package at.technikum.model.repository;

import at.technikum.model.card.ICard;
import at.technikum.model.card.cardTypes.CardType;

public interface Trade {

    String getTradeID();

    String getUserID();

    ICard getCard();

    double getMinPower();

    CardType getCardTyp();
}
