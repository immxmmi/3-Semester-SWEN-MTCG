package at.technikum.repository;

import at.technikum.database.IDBTable;
import at.technikum.model.IStack;

public interface IStackRepository extends IDBTable<IStack> {


    IStack addCardToStack(String userID, String cardID);

    IStack loadStack(String userID);

    IStack loadFreeStack(String userID);

}