package at.technikum.utils.stack.service;

import at.technikum.database.AbstractDBTable;
import at.technikum.utils.card.service.CardServices;
import at.technikum.utils.card.service.ICardServices;
import at.technikum.utils.cardHolder.service.CardHolderServices;
import at.technikum.utils.cardHolder.service.ICardHolderServices;
import at.technikum.utils.stack.IStack;
import at.technikum.utils.stack.Stack;

public class StackService extends AbstractDBTable implements IStackService {
    ICardHolderServices cardHolderServices;
    ICardServices cardServices;
    private static StackService instance;

    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public StackService() {
        this.tableName = "holder";
        this.cardHolderServices = new CardHolderServices();
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

    /**
     * --> PRINT STACK
     **/
    @Override
    public void printStack(IStack currentStack) {

        if (currentStack == null) {
            System.out.println(ANSI_RED + "NO STACK" + ANSI_RESET);
        }
       // printCards(currentStack.getStack(), "stack");
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
