package at.technikum.repository;

import at.technikum.database.AbstractDBTable;
import at.technikum.model.IStack;
import at.technikum.model.Stack;
import at.technikum.utils.card.service.CardServices;
import at.technikum.utils.card.service.ICardServices;

public class StackRepository extends AbstractDBTable implements IStackRepository {
    ICardHolderRepository cardHolderServices;
    ICardServices cardServices;

    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public StackRepository() {
        this.tableName = "holder";
        this.cardHolderServices = new CardHolderRepository();
        this.cardServices = new CardServices();
    }
    /*******************************************************************/


    /**
     * --> ADD Card tob Stack
     **/
    @Override
    public IStack addCardToStack(String userID, String cardID) {
        this.cardHolderServices.insertCardToHolder(userID, cardID, false);
        return loadStack(userID);
    }

    @Override
    public IStack loadStack(String userID) {

        IStack currentStack = Stack.builder()
                .userID(userID)
                .stack(this.cardHolderServices.loadCardsByHolderID(userID))
                .build();
        return currentStack;
    }

    @Override
    public IStack loadFreeStack(String userID) {
        IStack currentStack = Stack.builder()
                .userID(userID)
                .stack(this.cardHolderServices.loadUnlockedCardsByHolderID(userID))
                .build();
        return currentStack;
    }




    /*******************************************************************/
    /**                     Datenbank - Operatoren                    **/
    /*******************************************************************/

    /**
     * --> Fügt CARDHOLDER in die Datenbank
     **/
    @Override
    public IStack insert(IStack item) {


        this.setStatement("INSERT INTO " + this.tableName + "(user_id,card_id_1,card_id_2,card_id_3,card_id_4)VALUES(?,?,?,?,?);", this.parameter);

        return null;
    }

    @Override
    public IStack update(IStack item) {

        this.setStatement("UPDATE " + this.tableName + " SET user_id = ?, card_id_1 = ?, card_id_2 = ?,card_id_3 = ?, card_id_4 = ? " + "WHERE user_id = '" + 2 + "' ;", this.parameter);
        return null;
    }

    @Override
    public boolean delete(IStack item) {
        //System.out.println(ANSI_BLUE + "#DELETE:" + ANSI_RESET);

        this.setStatement("DELETE FROM " + this.tableName +
                        " WHERE user_id = '" + 2 + "';"
                , this.parameter);
        return true;
    }
    /*******************************************************************/
}
