package at.technikum.model.card;

import at.technikum.model.card.cardTypes.CardElement;
import at.technikum.model.card.cardTypes.CardName;
import at.technikum.model.card.cardTypes.CardType;

public interface Card {

    String getCardID();

    CardName getCardName();

    CardType getCardType();

    CardElement getCardElement();

    double getCardPower();
}
