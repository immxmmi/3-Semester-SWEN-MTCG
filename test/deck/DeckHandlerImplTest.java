package deck;

import at.technikum.handler.CardHandlerImpl;
import at.technikum.handler.CardHolderHandlerImpl;
import at.technikum.handler.DeckHandlerImpl;
import at.technikum.handler.PlayerHandlerImpl;
import at.technikum.handler.repository.CardHandler;
import at.technikum.handler.repository.CardHolderHandler;
import at.technikum.handler.repository.DeckHandler;
import at.technikum.handler.repository.PlayerHandler;
import at.technikum.model.DeckImpl;
import at.technikum.model.PlayerImpl;
import at.technikum.model.card.Card;
import at.technikum.model.card.cardTypes.CardElement;
import at.technikum.model.card.cardTypes.CardName;
import at.technikum.model.card.cardTypes.CardType;
import at.technikum.model.repository.Deck;
import at.technikum.model.repository.Player;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckHandlerImplTest {

    @Mock
    DeckHandlerImpl deckHandler = new DeckHandlerImpl();

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
    private Deck TestDeck(){
        Player player = getTestUser();
        CardHolderHandler cardHolderHandler = new CardHolderHandlerImpl();
        ArrayList<Card> cards = new ArrayList<>();
        ArrayList<String> cardIDs = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            cardHolderHandler.insertCardToHolder(player.getUserID(),"test"+i,false);
            cards.add(createTestCard("test"+i));
            cardIDs.add("test"+i);
        }

        return DeckImpl.builder()
                .userID(player.getUserID())
                .cardIDs(cardIDs)
                .deckList(cards)
                .build();
    }

    @Test
    void setNewDeck() {
        Deck deck = TestDeck();
        deckHandler.setNewDeck(deck.getCardIDs(),deck.getUserID());
        assertNotNull(deckHandler.getItemById(deck.getUserID()));
        delete(deck.getUserID(),deck.getDeckList(),deck);
    }

    @Test
    void getItemById() {
        Deck deck = TestDeck();
        deck = deckHandler.getItemById(deck.getUserID());
        assertNotNull(deck);
        delete(deck.getUserID(),deck.getDeckList(),deck);
    }


    @Test
    void delete(String playerID, List<Card> cards, Deck deck) {
        PlayerHandler playerHandler = new PlayerHandlerImpl();
        CardHandler cardHandler = new CardHandlerImpl();
        CardHolderHandler cardHolderHandler = new CardHolderHandlerImpl();

        assertNotNull(deck);
        deckHandler.delete(deck);

        for(Card card : cards){
            assertNotNull(card);
             cardHolderHandler.removeCardFromStack(playerID,card.getCardID());
             cardHandler.delete(card);
        }

        Player player = playerHandler.getItemById(playerID);
        assertNotNull(player);
        playerHandler.delete(player);


    }

    @Test
    void checkDeck() {
        Deck deck = TestDeck();
        assertNotNull(deck.getCardIDs());
        assertNotNull(deck.getUserID());
        deckHandler.checkDeck(deck.getCardIDs(),deck.getUserID());
        
    }
}