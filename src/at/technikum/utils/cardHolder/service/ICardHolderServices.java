package at.technikum.utils.cardHolder.service;

import at.technikum.database.IDBTable;
import at.technikum.utils.card.ICard;
import at.technikum.utils.cardHolder.ICardHolder;
import at.technikum.utils.player.IPlayer;

import java.util.ArrayList;

public interface ICardHolderServices extends IDBTable<ICardHolder> {
    /**
     * --> Läd alle Karten aus der Datenbank die dem User gehöhren in einem Array
     **/
    ArrayList<ICard> loadCardsByHolderID(String holderID);

    /**
     * --> Läd alle freien Karten aus der Datenbank die dem User gehöhren in einem Array
     **/
    ArrayList<ICard> loadUnlockedCardsByHolderID(String holderID);

    /**
     * --> dient zum Verkauf eines Packages an einem User
     **/
    void sellPackage(String packageID, String new_holderID);

    /**
     * --> ändert beim Verkauf einer Karte den Besitzer
     **/
    void changeCardHolder(String new_holderID, String old_holderID, String cardID);

    void switchCardHolder(String holderID_1, String holderID_2, String cardID_1, String cardID_2);

    void packageToNewHolder(String packageID, String new_holderID);

    // double getCardHolderLockedNumber(String holderID, String cardID);

    // double getCardHolderUnLockedNumber(String holderID, String cardID);

    ICardHolder getCardHolder(String holderID, String cardID);

    ICardHolder getCardHolderByID(String cardHolderID);

    CardHolderServices getInstance();

    void insertCardToHolder(String holderID, String cardID, boolean locked);

    void updateLocked(ICardHolder card, Boolean locked);

    boolean deleteAllUserCards(IPlayer currentPlayer);

    void removeCardFromStack(String holderID, String cardID);

    //  void deleteHolderByID(String packageID);
}
