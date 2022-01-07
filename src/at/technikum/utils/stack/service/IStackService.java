package at.technikum.utils.stack.service;

import at.technikum.database.IDBTable;
import at.technikum.utils.stack.IStack;

public interface IStackService extends IDBTable<IStack> {


    IStack addCardToStack(String userID, String cardID);

    IStack loadStack(String userID);

    IStack loadFreeStack(String userID);

    void printStack(IStack currentStack);
}