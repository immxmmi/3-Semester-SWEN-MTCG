package stack;

import at.technikum.handler.*;
import at.technikum.handler.repository.CardHandler;
import at.technikum.handler.repository.CardHolderHandler;
import at.technikum.handler.repository.PlayerHandler;
import at.technikum.model.DeckImpl;
import at.technikum.model.PlayerImpl;
import at.technikum.model.StackImpl;
import at.technikum.model.card.Card;
import at.technikum.model.card.cardTypes.CardElement;
import at.technikum.model.card.cardTypes.CardName;
import at.technikum.model.card.cardTypes.CardType;
import at.technikum.model.repository.Deck;
import at.technikum.model.repository.Player;
import at.technikum.model.repository.Stack;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StackHandlerImplTest {

    @Mock
    StackHandlerImpl stackHandler = new StackHandlerImpl();

    private Player createTestUser(String letter){
        return PlayerImpl.builder()
                .userID("Token"+letter)
                .username("TestUser"+letter)
                .password("pw")
                .build();

    }
    private Player getTestUser(){
        Player player = createTestUser("Deck");
        PlayerHandler playerHandler = new PlayerHandlerImpl();
        assertNotNull(player);
        playerHandler.register(player.getUserID(),player.getUsername(),player.getPassword());
        assertNotNull(playerHandler.getPlayerByUsername(player.getUsername()));
        assertNotNull(playerHandler.getItemById(player.getUserID()));
        return player;
    }
    private Card createTestCard(String id){
        CardHandler cardHandler = new CardHandlerImpl();
        cardHandler.addCardByData(id, CardName.Dragon, CardType.MONSTER, CardElement.NORMAL,200.00);
        return cardHandler.getCardById(id);
    }

    @Test
    void addCardToStack() {
        Player player = getTestUser();
        Stack stack = stackHandler.getItemById(player.getUserID());
        stackHandler.addCardToStack(stack.getUserID(),createTestCard("StackA").getCardID());
        stackHandler.addCardToStack(stack.getUserID(),createTestCard("StackB").getCardID());
        stackHandler.addCardToStack(stack.getUserID(),createTestCard("StackC").getCardID());
        stackHandler.addCardToStack(stack.getUserID(),createTestCard("StackD").getCardID());
        stack = stackHandler.getItemById(stack.getUserID());
        assertEquals(stack.getStack().size(),4);
        delete(stack);
    }

    private void delete(Stack stack) {
        PlayerHandler playerHandler = new PlayerHandlerImpl();
        CardHandler cardHandler = new CardHandlerImpl();
        CardHolderHandler cardHolderHandler = new CardHolderHandlerImpl();
        assertNotNull(stack);

        for(Card card : stack.getStack()){
            assertNotNull(card);
            cardHolderHandler.removeCardFromStack(stack.getUserID(),card.getCardID());
            cardHandler.delete(card);
        }

        Player player = playerHandler.getItemById(stack.getUserID());
        assertNotNull(player);
        playerHandler.delete(player);

    }
}