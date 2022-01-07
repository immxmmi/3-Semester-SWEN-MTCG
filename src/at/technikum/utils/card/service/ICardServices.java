package at.technikum.utils.card.service;

import at.technikum.utils.card.ICard;
import at.technikum.utils.card.cardTypes.CardElement;
import at.technikum.utils.card.cardTypes.CardName;
import at.technikum.utils.card.cardTypes.CardType;

import java.util.ArrayList;
import java.util.HashMap;

public interface ICardServices {
    /** --> wandelt getAllCards in ArrayListen um **/
    ArrayList<ICard> getAllCardsList();

    /** --> erstellt Default Karten **/
    void DefaultCards();

    /** --> Liefert eine Karte zurück **/
    ICard cardBuilder(String cardID, String cardName, CardType cardTyp, String cardElement, String cardPower);

    /** Liefert eine Karte mithilfe --> ID aus der Datenbank **/
    ICard getCardById(String cardID);

    /** Liefert eine Karte mithilfe --> Name aus der Datenbank **/
    ICard getCardByName(String cardName);

    /** --> Listet alle Karten in einer HashMap <Karten, KartenName> **/
    HashMap<ICard, CardName> getAllCards(String attribute, String value, boolean filter);

    /** --> Filtert aus dem Namen den CardType heraus **/
    CardType filterCardType(CardName cardName);

    /** --> Filtert aus dem Namen das Element heraus **/
    CardElement filterCardElement(CardName cardName);

    CardServices getInstance();

    /** --> Funkiton fügt Karte mit Daten zur Datenbank hinzugefügt **/
    ICard addCardByData(String cardID,CardName cardName, CardType cardTyp, CardElement cardElement, double cardPower);

    /** Fügt Karte zur Datenbank hinzu **/
    ICard insert(ICard newCard);

    /** Verändert die Karte **/
    ICard update(ICard currentCard);

    /** Löscht die Karte **/
    boolean delete(ICard currentCard);

}
