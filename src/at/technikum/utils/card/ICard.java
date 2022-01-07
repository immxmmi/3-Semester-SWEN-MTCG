package at.technikum.utils.card;

import at.technikum.utils.card.cardTypes.CardElement;
import at.technikum.utils.card.cardTypes.CardName;
import at.technikum.utils.card.cardTypes.CardType;

public interface ICard {

    String getCardID();

    CardName getCardName();

    CardType getCardType();

    CardElement getCardElement();

    double getCardPower();
}
