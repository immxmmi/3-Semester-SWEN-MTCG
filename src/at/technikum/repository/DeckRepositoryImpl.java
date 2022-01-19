package at.technikum.repository;

import at.technikum.database.AbstractDBTable;
import at.technikum.model.DeckImpl;
import at.technikum.model.IDeck;
import at.technikum.utils.card.ICard;
import at.technikum.utils.card.service.CardServices;
import at.technikum.utils.card.service.ICardServices;
import at.technikum.utils.tools.TextColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeckRepositoryImpl extends AbstractDBTable implements DeckRepository {

    CardHolderRepository cardHolderServices;
    ICardServices cardServices;
    private static DeckRepositoryImpl instance;

    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public DeckRepositoryImpl() {
        this.tableName = "deck";
        this.cardHolderServices = new CardHolderRepositoryImpl();
        this.cardServices = new CardServices();
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                            Builder                            **/
    /*******************************************************************/
    private IDeck deckBuilder(ResultSet result) {
        try {
            if (result.next()) {
                ArrayList<String> currentCardIDs = new ArrayList<>();
                List<ICard> currentDeck = new ArrayList<>();
                String userID = result.getString("user_id");

                currentCardIDs.add(result.getString("card_id_1"));
                currentCardIDs.add(result.getString("card_id_2"));
                currentCardIDs.add(result.getString("card_id_3"));
                currentCardIDs.add(result.getString("card_id_4"));

                currentDeck.add(this.cardServices.getCardById(currentCardIDs.get(0)));
                currentDeck.add(this.cardServices.getCardById(currentCardIDs.get(1)));
                currentDeck.add(this.cardServices.getCardById(currentCardIDs.get(2)));
                currentDeck.add(this.cardServices.getCardById(currentCardIDs.get(3)));


                IDeck deck = DeckImpl.builder()
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


    private boolean checkDeck(ArrayList<String> newDeck, String holderID) {
        CardHolderRepository cardHolderServices = new CardHolderRepositoryImpl();
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

        IDeck oldDeck = getDeckById(holderID);
        boolean createNewDeck = false;
        if (oldDeck == null) {
            createNewDeck = true;
        }

        IDeck currentDeck = DeckImpl.builder()
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

    private void lockeCards(ArrayList<String> newDeck, IDeck oldDeck) {
        for (int i = 0; i < newDeck.size(); i++) {
            if (oldDeck != null) {
                this.cardHolderServices.updateLocked(this.cardHolderServices.getCardHolder(oldDeck.getUserID(), oldDeck.getDeckList().get(i).getCardID()), false);
            }
            this.cardHolderServices.updateLocked(this.cardHolderServices.getCardHolder(oldDeck.getUserID(), newDeck.get(i)), true);
        }
    }

    @Override
    public DeckRepositoryImpl getInstance() {
        if (DeckRepositoryImpl.instance == null) {
            DeckRepositoryImpl.instance = new DeckRepositoryImpl();
        }
        return DeckRepositoryImpl.instance;
    }

    @Override
    public IDeck getDeckById(String userID) {
        this.parameter = new String[]{userID};

        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE user_id = ? " + ";",
                this.parameter
        );
        return deckBuilder(this.result);
    }

    @Override
    public IDeck insert(IDeck item) {

        String userID = item.getUserID();

        this.parameter = new String[]{
                "" + userID,
                "" + item.getCardIDs().get(0),
                "" + item.getCardIDs().get(1),
                "" + item.getCardIDs().get(2),
                "" + item.getCardIDs().get(3)
        };


        this.setStatement("INSERT INTO " + this.tableName + "(user_id,card_id_1,card_id_2,card_id_3,card_id_4)VALUES(?,?,?,?,?);", this.parameter);
        return getDeckById(userID);
    }

    @Override
    public IDeck update(IDeck item) {

        String userID = item.getUserID();

        this.parameter = new String[]{
                "" + userID,
                "" + item.getCardIDs().get(0),
                "" + item.getCardIDs().get(1),
                "" + item.getCardIDs().get(2),
                "" + item.getCardIDs().get(3)
        };

        this.setStatement("UPDATE " + this.tableName + " SET user_id = ?, card_id_1 = ?, card_id_2 = ?,card_id_3 = ?, card_id_4 = ? " + "WHERE user_id = '" + userID + "' ;", this.parameter);
        return getDeckById(userID);
    }

    @Override
    public boolean delete(IDeck item) {
        //System.out.println(ANSI_BLUE + "#DELETE:" + ANSI_RESET);
        this.parameter = new String[]{};
        String userID = item.getUserID();
        if (getDeckById(userID) == null) {
            return false;
        }
        this.setStatement("DELETE FROM " + this.tableName +
                        " WHERE user_id = '" + userID + "';"
                , this.parameter);
        return true;
    }
}