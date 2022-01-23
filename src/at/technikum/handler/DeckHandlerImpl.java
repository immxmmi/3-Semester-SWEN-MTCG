package at.technikum.handler;

import at.technikum.database.AbstractDBTable;
import at.technikum.handler.repository.CardHandler;
import at.technikum.handler.repository.CardHolderHandler;
import at.technikum.handler.repository.DeckHandler;
import at.technikum.model.DeckImpl;
import at.technikum.model.card.Card;
import at.technikum.model.repository.Deck;
import at.technikum.utils.TextColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeckHandlerImpl extends AbstractDBTable implements DeckHandler {

    CardHolderHandler cardHolderServices;
    CardHandler cardServices;
    private static DeckHandlerImpl instance;

    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public DeckHandlerImpl() {
        this.tableName = "deck";
        this.cardHolderServices = new CardHolderHandlerImpl();
        this.cardServices = new CardHandlerImpl();
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                            Builder                            **/
    /*******************************************************************/
    private Deck deckBuilder(ResultSet result) {
        try {
            if (result.next()) {
                ArrayList<String> currentCardIDs = new ArrayList<>();
                List<Card> currentDeck = new ArrayList<>();
                String userID = result.getString("user_id");

                currentCardIDs.add(result.getString("card_id_1"));
                currentCardIDs.add(result.getString("card_id_2"));
                currentCardIDs.add(result.getString("card_id_3"));
                currentCardIDs.add(result.getString("card_id_4"));

                currentDeck.add(this.cardServices.getCardById(currentCardIDs.get(0)));
                currentDeck.add(this.cardServices.getCardById(currentCardIDs.get(1)));
                currentDeck.add(this.cardServices.getCardById(currentCardIDs.get(2)));
                currentDeck.add(this.cardServices.getCardById(currentCardIDs.get(3)));


                Deck deck = DeckImpl.builder()
                        .userID(userID)
                        .deckList(currentDeck)
                        .cardIDs(currentCardIDs)
                        .build();

                this.closeStatement();

                return deck;
            }
        } catch (SQLException e) {

            System.out.println(TextColor.ANSI_RED + "GETOBJECT -ERRROR: " + e + TextColor.ANSI_RESET);
            e.printStackTrace();
        }
        this.closeStatement();
        return null;
    }

    /*******************************************************************/


    public boolean checkDeck(ArrayList<String> newDeck, String holderID) {
        CardHolderHandler cardHolderServices = new CardHolderHandlerImpl();
        for (int i = 0; i < newDeck.size(); i++) {
            if (cardHolderServices.getCardHolder(holderID, newDeck.get(1)) == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean setNewDeck(ArrayList<String> newDeck, String holderID) {

        if (newDeck == null) {
            return false;
        }
        if (newDeck.size() != 4) {
            return false;
        }
        if (checkDeck(newDeck, holderID) == false) {
            return false;
        }

        Deck oldDeck = getItemById(holderID);
        boolean createNewDeck = false;
        if (oldDeck == null) {
            createNewDeck = true;
        }

        Deck currentDeck = DeckImpl.builder()
                .userID(holderID)
                .cardIDs(newDeck)
                .build();


        if (createNewDeck) {

            insert(currentDeck);

        } else {

            update(currentDeck);
        }

        //lockeCards(newDeck,oldDeck);

        return true;
    }

    private void lockeCards(ArrayList<String> newDeck, Deck oldDeck) {
        for (int i = 0; i < newDeck.size(); i++) {
            if (oldDeck != null) {
                this.cardHolderServices.updateLocked(this.cardHolderServices.getCardHolder(oldDeck.getUserID(), oldDeck.getDeckList().get(i).getCardID()), false);
            }
            this.cardHolderServices.updateLocked(this.cardHolderServices.getCardHolder(oldDeck.getUserID(), newDeck.get(i)), true);
        }
    }

    @Override
    public DeckHandlerImpl getInstance() {
        if (DeckHandlerImpl.instance == null) {
            DeckHandlerImpl.instance = new DeckHandlerImpl();
        }
        return DeckHandlerImpl.instance;
    }

    @Override
    public Deck getItemById(String userID) {
        this.parameter = new String[]{userID};

        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE user_id = ? " + ";",
                this.parameter
        );
        return deckBuilder(this.result);
    }

    @Override
    public Deck insert(Deck item) {

        String userID = item.getUserID();

        this.parameter = new String[]{
                "" + userID,
                "" + item.getCardIDs().get(0),
                "" + item.getCardIDs().get(1),
                "" + item.getCardIDs().get(2),
                "" + item.getCardIDs().get(3)
        };


        this.setStatement("INSERT INTO " + this.tableName + "(user_id,card_id_1,card_id_2,card_id_3,card_id_4)VALUES(?,?,?,?,?);", this.parameter);
        return getItemById(userID);
    }

    @Override
    public Deck update(Deck item) {

        String userID = item.getUserID();

        this.parameter = new String[]{
                "" + userID,
                "" + item.getCardIDs().get(0),
                "" + item.getCardIDs().get(1),
                "" + item.getCardIDs().get(2),
                "" + item.getCardIDs().get(3)
        };

        this.setStatement("UPDATE " + this.tableName + " SET user_id = ?, card_id_1 = ?, card_id_2 = ?,card_id_3 = ?, card_id_4 = ? " + "WHERE user_id = '" + userID + "' ;", this.parameter);
        return getItemById(userID);
    }

    @Override
    public boolean delete(Deck item) {
        //System.out.println(ANSI_BLUE + "#DELETE:" + ANSI_RESET);
        String userID = item.getUserID();
        if (getItemById(userID) == null) {
            return false;
        }
        this.parameter = new String[]{
                item.getUserID()
        };

        this.setStatement("DELETE FROM " + this.tableName +
                        " WHERE user_id = ? ;"
                , this.parameter);
        return true;
    }
}
