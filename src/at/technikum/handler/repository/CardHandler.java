package at.technikum.handler.repository;

import at.technikum.handler.CardHandlerImpl;
import at.technikum.model.card.Card;
import at.technikum.model.card.cardTypes.CardElement;
import at.technikum.model.card.cardTypes.CardName;
import at.technikum.model.card.cardTypes.CardType;

import java.util.ArrayList;
import java.util.HashMap;

public interface CardHandler {
    /** --> wandelt getAllCards in ArrayListen um **/
    ArrayList<Card> getAllCardsList();

    /** --> erstellt Default Karten **/
    void DefaultCards();

    /** --> Liefert eine Karte zurück **/
    Card cardBuilder(String cardID, String cardName, CardType cardTyp, String cardElement, String cardPower);

    /** Liefert eine Karte mithilfe --> ID aus der Datenbank **/
    Card getCardById(String cardID);

    /** Liefert eine Karte mithilfe --> Name aus der Datenbank **/
    Card getCardByName(String cardName);

    /** --> Listet alle Karten in einer HashMap <Karten, KartenName> **/
    HashMap<Card, CardName> getAllCards(String attribute, String value, boolean filter);

    /** --> Filtert aus dem Namen den CardType heraus **/
    CardType filterCardType(CardName cardName);

    /** --> Filtert aus dem Namen das Element heraus **/
    CardElement filterCardElement(CardName cardName);

    CardHandlerImpl getInstance();

    /** --> Funkiton fügt Karte mit Daten zur Datenbank hinzugefügt **/
    Card addCardByData(String cardID, CardName cardName, CardType cardTyp, CardElement cardElement, double cardPower);

    /** Fügt Karte zur Datenbank hinzu **/
    Card insert(Card newCard);

    /** Verändert die Karte **/
    Card update(Card currentCard);

    /** Löscht die Karte **/
    boolean delete(Card currentCard);

}
