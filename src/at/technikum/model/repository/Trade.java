package at.technikum.model.repository;

import at.technikum.model.card.Card;
import at.technikum.model.card.cardTypes.CardType;

public interface Trade {

    String getTradeID();

    String getUserID();

    Card getCard();

    double getMinPower();

    CardType getCardTyp();
}
