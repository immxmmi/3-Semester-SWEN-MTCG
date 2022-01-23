package at.technikum.handler;

import at.technikum.database.AbstractDBTable;
import at.technikum.handler.repository.CardHandler;
import at.technikum.handler.repository.CardHolderHandler;
import at.technikum.handler.repository.StackHandler;
import at.technikum.model.StackImpl;
import at.technikum.model.repository.Stack;

public class StackHandlerImpl extends AbstractDBTable implements StackHandler {
    CardHolderHandler cardHolderServices;
    CardHandler cardServices;

    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public StackHandlerImpl() {
        this.tableName = "holder";
        this.cardHolderServices = new CardHolderHandlerImpl();
        this.cardServices = new CardHandlerImpl();
    }
    /*******************************************************************/


    /**
     * --> ADD Card tob Stack
     **/
    @Override
    public Stack addCardToStack(String userID, String cardID) {
        this.cardHolderServices.insertCardToHolder(userID, cardID, false);
        return getItemById(userID);
    }

    @Override
    public Stack getItemById(String userID) {

        Stack currentStack = StackImpl.builder()
                .userID(userID)
                .stack(this.cardHolderServices.loadCardsByHolderID(userID))
                .build();
        return currentStack;
    }

    @Override
    public Stack loadFreeStack(String userID) {
        Stack currentStack = StackImpl.builder()
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
    public Stack insert(Stack item) {


        this.setStatement("INSERT INTO " + this.tableName + "(user_id,card_id_1,card_id_2,card_id_3,card_id_4)VALUES(?,?,?,?,?);", this.parameter);

        return null;
    }

    @Override
    public Stack update(Stack item) {

        this.setStatement("UPDATE " + this.tableName + " SET user_id = ?, card_id_1 = ?, card_id_2 = ?,card_id_3 = ?, card_id_4 = ? " + "WHERE user_id = '" + 2 + "' ;", this.parameter);
        return null;
    }

    @Override
    public boolean delete(Stack item) {
        //System.out.println(ANSI_BLUE + "#DELETE:" + ANSI_RESET);

        if (getItemById(item.getUserID()) == null) {
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
    /*******************************************************************/
}
