package at.technikum.handler;

import at.technikum.database.AbstractDBTable;
import at.technikum.handler.repository.CardHolderHandler;
import at.technikum.handler.repository.PackageHandler;
import at.technikum.handler.repository.Repository;
import at.technikum.model.CardHolderImpl;
import at.technikum.model.card.Card;
import at.technikum.model.repository.CardHolder;
import at.technikum.model.repository.Player;
import at.technikum.utils.TextColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CardHolderHandlerImpl extends AbstractDBTable implements CardHolderHandler, Repository<CardHolder> {

    private static CardHolderHandlerImpl instance;

    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public CardHolderHandlerImpl() {
        this.tableName = "cardHolder";
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                            Builder                            **/
    /*******************************************************************/
    /**
     * --> Baut eine CARDHOLDER KLASSE
     **/
    public CardHolder cardHolderBuilder(ResultSet result) {
        try {
            if (result.next()) {

                //   System.out.println(this.result.getString("cardHolder_id"));

                CardHolder holder = CardHolderImpl.builder()
                        .cardHolderID(this.result.getString("cardHolder_id"))
                        .cardID(this.result.getString("card_id"))
                        .number( this.tools.convertToDouble(this.result.getString("number")))
                        .holderID("holder_id")
                        .locked( this.tools.convertToBoolean(this.result.getString("locked")))
                        .build();
                // this.closeStatement();

                return holder;
            }
        } catch (SQLException e) {

            System.out.println(TextColor.ANSI_RED + "GETOBJECT -ERRROR: " + e + TextColor.ANSI_RESET);
            e.printStackTrace();
        }
        // this.closeStatement();
        return null;
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                              LOAD                             **/
    /*******************************************************************/

    /**
     * --> Läd alle Karten aus der Datenbank die dem User gehöhren in einem Array
     **/
    @Override
    public ArrayList<Card> loadCardsByHolderID(String holderID) {
        this.parameter = new String[]{holderID};
        ArrayList<Card> cards = new ArrayList<>();
        CardHandlerImpl card = new CardHandlerImpl();

        this.setStatement("SELECT  *  FROM \"cardHolder\" WHERE holder_id = ? ;", this.parameter);

        try {
            while (this.result.next()) {

                for (int i = 0; i <  this.tools.convertToDouble(this.result.getString("number")); i++) {
                    cards.add(card.getItemById(result.getString("card_id")));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cards;
    }

    /**
     * --> Läd alle freien Karten aus der Datenbank die dem User gehöhren in einem Array
     **/
    @Override
    public ArrayList<Card> loadUnlockedCardsByHolderID(String holderID) {
        this.parameter = new String[]{holderID, "" + false};
        ArrayList<Card> cards = new ArrayList<>();
        CardHandlerImpl card = new CardHandlerImpl();

        this.setStatement("SELECT  *  FROM \"cardHolder\" WHERE holder_id = ? AND locked = ? ;", this.parameter);

        try {
            while (this.result.next()) {

                for (int i = 0; i <  this.tools.convertToDouble(this.result.getString("number")); i++) {
                    cards.add(card.getItemById(result.getString("card_id")));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cards;
    }

    /*******************************************************************/

    /*******************************************************************/
    /**                            ACTION                             **/
    /*******************************************************************/

    /**
     * --> dient zum Verkauf eines Packages an einem User
     **/
    @Override
    public void sellPackage(String packageID, String new_holderID) {

        PackageHandler packageService = new PackageHandlerImpl();

        //   System.out.println("#PACKAGE STORE");
        this.parameter = new String[]{packageID};
        this.setStatement("SELECT card_id , number FROM \"" + this.tableName + "\" WHERE holder_id = ? ;", this.parameter);
        ResultSet first_result = this.result;
        try {
            while (first_result.next()) {
                for (int i = 0; i <  this.tools.convertToDouble(first_result.getString("number")); i++) {
                    insertCardToHolder(new_holderID, first_result.getString("card_id"), false);
                }
                ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.closeStatement();

//        System.out.println("DELETE HOLDER:");
        this.deleteHolderByID(packageID);
        //      System.out.println("DELETE PACKAGE:");
        packageService.deletePackageByID(packageID);
        //    System.out.println("PACKAGE SOLD");
        //    System.out.println("NEW PACKAGE");
        //  packageService.CreateDefaultPackages(1);
    }

    /**
     * --> ändert beim Verkauf einer Karte den Besitzer
     **/
    @Override
    public void changeCardHolder(String new_holderID, String old_holderID, String cardID) {

        double number = getCardHolderNumber(old_holderID, cardID, true);

        //System.out.println("NUMBER DAVOR: " + number);

        if (number < 2) {
            delete(this.getCardHolder(old_holderID, cardID));
        } else {
            number--;
           // System.out.println("NUMBER: " + number);
            this.parameter = new String[]{"" + number, old_holderID, cardID};
            this.setStatement("UPDATE  \"" + this.tableName + "\" SET  number = ? WHERE holder_id = ? AND card_id = ?;", this.parameter);
        }
        insertCardToHolder(new_holderID, cardID, false);

    }

    /**
     * --> Karten tausch
     **/
    @Override
    public boolean switchCardHolder(String holderID_1, String holderID_2, String cardID_1, String cardID_2) {
        changeCardHolder(holderID_1, holderID_2, cardID_2);
        changeCardHolder(holderID_2, holderID_1, cardID_1);
        return true;
    }

    /**
     * --> verkauft Package an User
     **/
    @Override
    public void packageToNewHolder(String packageID, String new_holderID) {
        this.parameter = new String[]{new_holderID, "" + false, packageID};
        this.setStatement("UPDATE  \"" + this.tableName + "\" SET  holder_id = ? locked = ? WHERE holder_id = ? ;", this.parameter);
        this.closeStatement();
    }

    /*******************************************************************/


    /*******************************************************************/
    /**                            GETTER                             **/
    /*******************************************************************/
    /**
     * --> checkt, ob die kompination [cardID] + [holderID] in der Datenbank vorhanden ist
     * es ist zu unterscheiden zwischen gesperrte und nicht gesperrte Karten.
     **/
    private double getCardHolderNumber(String holderID, String cardID, boolean locked) {
        // System.out.println("#CHECK NUMBER:");
        if (locked) {
            return getCardHolderLockedNumber(holderID, cardID);
        }
        return getCardHolderUnLockedNumber(holderID, cardID);
    }

    /**
     * --> gibt die Anzahl der gesperrten Karten an
     **/
    private double getCardHolderLockedNumber(String holderID, String cardID) {
        //  System.out.println("CARDHOLDER - CHECK - LOCKED");
        this.parameter = new String[]{holderID, cardID, "true"};
        this.setStatement("SELECT  *  FROM \"cardHolder\" WHERE holder_id = ? AND card_id = ? AND locked = ? ;", this.parameter);
        CardHolder holder = this.cardHolderBuilder(this.result);
        if (holder == null) {
            return 0;
        }
        return holder.getNumber();
    }

    /**
     * --> gibt die Anzahl der ungesperrten Karten an
     **/
    private double getCardHolderUnLockedNumber(String holderID, String cardID) {
        // System.out.println("CARDHOLDER - CHECK - UNLOCKED");
        this.parameter = new String[]{holderID, cardID, "false"};
        this.setStatement("SELECT  *  FROM \"cardHolder\" WHERE holder_id = ? AND card_id = ? AND locked = ?;", this.parameter);
        CardHolder holder = this.cardHolderBuilder(this.result);
        if (holder == null) {
            return 0;
        }
        return holder.getNumber();
    }

    /**
     * --> Liefert cardHolder Objekt by [holderID] [cardID]
     **/
    @Override
    public CardHolder getCardHolder(String holderID, String cardID) {
        this.parameter = new String[]{holderID, cardID};
        this.setStatement("SELECT  *  FROM \"cardHolder\" WHERE holder_id = ? AND card_id = ?;", this.parameter);
        //  System.out.println(this.statement);
        return this.cardHolderBuilder(this.result);
    }

    /**
     * --> Liefert cardHolder Objekt by [cardHolderID]
     **/
    @Override
    public CardHolder getItemById(String cardHolderID) {
        this.parameter = new String[]{cardHolderID};
        this.setStatement("SELECT  *  FROM \"cardHolder\" WHERE cardHolder_id = ? ;", this.parameter);
        return this.cardHolderBuilder(this.result);

    }

    @Override
    public CardHolderHandlerImpl getInstance() {
        if (CardHolderHandlerImpl.instance == null) {
            CardHolderHandlerImpl.instance = new CardHolderHandlerImpl();
        }
        return CardHolderHandlerImpl.instance;
    }

    /*******************************************************************/



    /*******************************************************************/
    /**                     Datenbank - Operatoren                    **/
    /*******************************************************************/

    /**
     * --> Fügt CARDHOLDER in die Datenbank
     **/
    @Override
    public CardHolder insert(CardHolder holder) {
        //  System.out.println("INSERT CARDHOLDER: ");
        this.parameter = new String[]{holder.getCardHolderID(), holder.getHolderID(), holder.getCardID(), "" + holder.getNumber(), "" + holder.isLocked()};
        this.setStatement("INSERT INTO \"" + this.tableName + "\" (\"cardHolder_id\", holder_id, card_id, number, locked) VALUES(?,?,?,?,?);", this.parameter);
        return getItemById(holder.getCardHolderID());
    }

    /**
     * --> fügt HOLDER in die Datenbank + Daten
     **/
    @Override
    public void insertCardToHolder(String holderID, String cardID, boolean locked) {
        //  System.out.println("#INSERT CARD TO HOLDER");
        String cardHolderID = "H-" +  this.tools.tokenSupplier.get();
        double number = getCardHolderNumber(holderID, cardID, locked);

        if (number == 0) {
            if (locked) {
                this.parameter = new String[]{cardHolderID, holderID, cardID, "1", "" + true};
            } else {
                this.parameter = new String[]{cardHolderID, holderID, cardID, "1", "" + false};
            }
            this.setStatement("INSERT INTO \"" + this.tableName + "\" (\"cardHolder_id\", holder_id, card_id, number, locked) VALUES(?,?,?,?,?);", this.parameter);
        } else {
            number++;
            this.updateNumber("" + number, cardID, holderID);
        }
    }

    /**
     * --> überschreibt CARDHOLDER
     **/
    @Override
    public CardHolder update(CardHolder cardHolder) {
        this.parameter = new String[]{
                cardHolder.getCardHolderID(),
                cardHolder.getHolderID(),
                cardHolder.getCardID(),
                "" + cardHolder.getNumber(),
                "" + cardHolder.isLocked(),
                cardHolder.getCardHolderID()
        };
        this.setStatement("UPDATE  \"" + this.tableName + "\" SET cardHolder_id = ? card_id = ? holder_id = ? number = ? locked = ? WHERE cardHolder_id = ?;", this.parameter);
        this.closeStatement();

        return cardHolder;
    }

    /**
     * --> Sperrt und Entsperrt Karten --> [locked]
     **/
    @Override
    public void updateLocked(CardHolder cardHolder, Boolean locked) {
        this.parameter = new String[]{"" + locked, cardHolder.getCardHolderID()};
        setStatement("UPDATE  \"" + this.tableName + "\" SET  locked = ? WHERE \"cardHolder_id\" = ?;", this.parameter);
    }

    /**
     * --> Ändert die Anzahl der Karten --> [number]
     **/
    private void updateNumber(String number, String card_id, String holder_id) {
        this.parameter = new String[]{number, card_id, holder_id};
        setStatement("UPDATE  \"" + this.tableName + "\" SET  number = ? WHERE card_id = ? AND holder_id = ?;", this.parameter);
    }

    /**
     * --> Löscht Item aus der Datenbank
     **/
    @Override
    public boolean delete(CardHolder item) {
        //  System.out.println("DELETE ITEM - ID "+ item.getCardHolderID());
        if (item == null){
            return false;
        }
        this.parameter = new String[]{item.getCardHolderID()};
        this.setStatement("DELETE FROM \"" + this.tableName + "\" WHERE \"cardHolder_id\" = ? ;", this.parameter);
        return true;
    }

    @Override
    public boolean deleteAllUserCards(Player currentPlayer) {
        for (Card card : currentPlayer.getStack().getStack()) {
            removeCardFromStack(currentPlayer.getUserID(), card.getCardID());
        }

        for (Card card : currentPlayer.getFreeStack().getStack()) {
            removeCardFromStack(currentPlayer.getUserID(), card.getCardID());
        }
        return true;
    }

    /**
     * --> entfernt Karten von Stack und fügt es im Deck
     **/
    @Override
    public void removeCardFromStack(String holderID, String cardID) {
        this.parameter = new String[]{holderID, cardID};
        this.setStatement("DELETE FROM \"" + this.tableName + "\" WHERE holder_id = ? AND card_id = ?;", this.parameter);
        closeStatement();
    }

    /**
     * --> Löscht Package mit der ID
     **/
    private void deleteHolderByID(String holderID) {
        this.parameter = new String[]{holderID};
        this.setStatement("DELETE FROM \"" + this.tableName + "\" WHERE holder_id = ? ;", this.parameter);
        closeStatement();
    }

    /*******************************************************************/

}
