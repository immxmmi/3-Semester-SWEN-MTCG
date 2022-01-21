package at.technikum.repository;

import at.technikum.net.database.AbstractDBTable;
import at.technikum.model.repository.Stack;
import at.technikum.model.StackImpl;
import at.technikum.utils.card.service.CardServices;
import at.technikum.utils.card.service.ICardServices;

public class StackRepositoryImpl extends AbstractDBTable implements StackRepository {
    CardHolderRepository cardHolderServices;
    ICardServices cardServices;

    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public StackRepositoryImpl() {
        this.tableName = "holder";
        this.cardHolderServices = new CardHolderRepositoryImpl();
        this.cardServices = new CardServices();
    }
    /*******************************************************************/


    /**
     * --> ADD Card tob Stack
     **/
    @Override
    public Stack addCardToStack(String userID, String cardID) {
        this.cardHolderServices.insertCardToHolder(userID, cardID, false);
        return loadStack(userID);
    }

    @Override
    public Stack loadStack(String userID) {

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

        this.setStatement("DELETE FROM " + this.tableName +
                        " WHERE user_id = '" + 2 + "';"
                , this.parameter);
        return true;
    }
    /*******************************************************************/
}
