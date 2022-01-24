package at.technikum.handler.repository;

import at.technikum.handler.CardHolderHandlerImpl;
import at.technikum.model.card.Card;
import at.technikum.model.repository.CardHolder;
import at.technikum.model.repository.Player;

import java.util.ArrayList;

// TODO: 23.01.2022 Fertig 
public interface CardHolderHandler {
    /**
     * --> Läd alle Karten aus der Datenbank die dem User gehöhren in einem Array
     **/
    ArrayList<Card> loadCardsByHolderID(String holderID);

    /**
     * --> Läd alle freien Karten aus der Datenbank die dem User gehöhren in einem Array
     **/
    ArrayList<Card> loadUnlockedCardsByHolderID(String holderID);

    /**
     * --> dient zum Verkauf eines Packages an einem User
     **/
    void sellPackage(String packageID, String new_holderID);

    /**
     * --> ändert beim Verkauf einer Karte den Besitzer
     **/
    void changeCardHolder(String new_holderID, String old_holderID, String cardID);

    boolean switchCardHolder(String holderID_1, String holderID_2, String cardID_1, String cardID_2);

    void packageToNewHolder(String packageID, String new_holderID);

    // double getCardHolderLockedNumber(String holderID, String cardID);

    // double getCardHolderUnLockedNumber(String holderID, String cardID);

    CardHolder getCardHolder(String holderID, String cardID);

    CardHolder getItemById(String cardHolderID);

    CardHolderHandlerImpl getInstance();

    void insertCardToHolder(String holderID, String cardID, boolean locked);

    void updateLocked(CardHolder card, Boolean locked);

    boolean deleteAllUserCards(Player currentPlayer);

    void removeCardFromStack(String holderID, String cardID);

}
