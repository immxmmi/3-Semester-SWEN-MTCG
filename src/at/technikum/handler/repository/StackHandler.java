package at.technikum.handler.repository;

import at.technikum.model.repository.Stack;
import at.technikum.handler.repository.Repository;

public interface StackHandler extends Repository<Stack> {


    Stack addCardToStack(String userID, String cardID);

    Stack getItemById(String userID);

    Stack loadFreeStack(String userID);

}