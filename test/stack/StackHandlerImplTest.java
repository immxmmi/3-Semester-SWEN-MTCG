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
    private Stack TestStack(){
        Player player = getTestUser();
        CardHolderHandler cardHolderHandler = new CardHolderHandlerImpl();
        ArrayList<Card> cards = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            cardHolderHandler.insertCardToHolder(player.getUserID(),"test"+i,false);
            cards.add(createTestCard("test"+i));
        }

        return StackImpl.builder()
                .userID(player.getUserID())
                .stack(cards)
                .build();
    }
    @Test
    void addCardToStack() {
        Player player = getTestUser();
        stackHandler.addCardToStack(createTestCard("StackA").getCardID(),player.getUserID());
        stackHandler.addCardToStack(createTestCard("StackB").getCardID(),player.getUserID());
        stackHandler.addCardToStack(createTestCard("StackC").getCardID(),player.getUserID());
        stackHandler.addCardToStack(createTestCard("StackD").getCardID(),player.getUserID());
        assertNotNull(stackHandler.getItemById(player.getUserID()).getStack());
    }

    @Test
    void getItemById() {
    }

    @Test
    void delete(Stack stack) {
        stackHandler.delete(stack);
    }
}