package at.technikum.repository;

import at.technikum.model.Stack;

public interface StackRepository extends Repository<Stack> {


    Stack addCardToStack(String userID, String cardID);

    Stack loadStack(String userID);

    Stack loadFreeStack(String userID);

}