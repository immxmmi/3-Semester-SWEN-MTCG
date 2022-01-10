package at.technikum.repository;

import at.technikum.database.DBTable;
import at.technikum.model.Stack;

public interface StackRepository extends DBTable<Stack> {


    Stack addCardToStack(String userID, String cardID);

    Stack loadStack(String userID);

    Stack loadFreeStack(String userID);

}