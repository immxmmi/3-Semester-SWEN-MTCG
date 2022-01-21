package at.technikum.repository;

import at.technikum.model.repository.Stack;
import at.technikum.repository.util.Repository;

public interface StackRepository extends Repository<Stack> {


    Stack addCardToStack(String userID, String cardID);

    Stack getItemById(String userID);

    Stack loadFreeStack(String userID);

}