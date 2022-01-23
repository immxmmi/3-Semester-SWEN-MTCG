package battle;

import at.technikum.handler.*;
import at.technikum.handler.repository.CardHandler;
import at.technikum.handler.repository.CardHolderHandler;
import at.technikum.handler.repository.DeckHandler;
import at.technikum.handler.repository.PlayerHandler;
import at.technikum.model.BattleImpl;
import at.technikum.model.DeckImpl;
import at.technikum.model.PlayerImpl;
import at.technikum.model.card.Card;
import at.technikum.model.card.cardTypes.*;
import at.technikum.model.repository.Battle;
import at.technikum.model.repository.Deck;
import at.technikum.model.repository.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BattleHandler")
class BattleHandlerImplTest {

    @Mock
    BattleHandlerImpl battleHandler = new BattleHandlerImpl();



    private Card createTestCard(CardType cardType, CardName cardName, CardElement cardElement, Double power) {
        if(cardType == CardType.MONSTER) {
                return MonsterCardImpl.builder()
                        .cardTyp(cardType)
                        .cardName(cardName)
                        .cardElement(cardElement)
                        .cardPower(power)
                        .build();
        }

        if(cardType == CardType.SPELL) {
            return SpellCardImpl.builder()
                    .cardTyp(cardType)
                    .cardName(cardName)
                    .cardElement(cardElement)
                    .cardPower(power)
                    .build();
        }

        return null;
    }
    private Card insertTestCard(String id,Card card){
        CardHandler cardHandler = new CardHandlerImpl();
        cardHandler.addCardByData(id, card.getCardName(), card.getCardType(), card.getCardElement(), card.getCardPower());
        return cardHandler.getCardById(id);
    }
    private Player createTestUser(String letter){
        return PlayerImpl.builder()
                .userID("Token"+letter)
                .username("Player"+letter)
                .elo(100.00)
                .password("pw")
                .build();

    }
    private Player getTestUser(String letter){
        Player player = createTestUser("Player"+letter);
        PlayerHandler playerHandler = new PlayerHandlerImpl();
        assertNotNull(player);
        playerHandler.register(player.getUserID(),player.getUsername(),player.getPassword());
        assertNotNull(playerHandler.getPlayerByUsername(player.getUsername()));
        assertNotNull(playerHandler.getItemById(player.getUserID()));
        return player;
    }
    private void createTestDecks(Player playerA, Player playerB) {
        ArrayList<Card> currentDeck = new ArrayList<>();
        ArrayList<String> cardIDs = new ArrayList<>();
        CardHolderHandler cardHolderHandler = new CardHolderHandlerImpl();
        DeckHandler deckHandler = new DeckHandlerImpl();
        // DECK A
        for(int i = 0; i < 4;  i++) {
            currentDeck.add(createTestCard(CardType.MONSTER, CardName.Dragon, CardElement.FIRE, 20000.0));
            assertNotNull(currentDeck.get(i));
            insertTestCard(playerA.getUserID() + i, currentDeck.get(i));
            cardHolderHandler.insertCardToHolder(playerA.getUserID(), playerA.getUserID() + i, false);
            cardIDs.add(playerA.getUserID() + i);
        }


        Deck deckA = DeckImpl.builder()
                .userID(playerA.getUserID())
                .deckList(currentDeck)
                .cardIDs(cardIDs)
                .build();

        deckHandler.setNewDeck(deckA.getCardIDs(),deckA.getUserID());

        currentDeck = new ArrayList<>();
        cardIDs = new ArrayList<>();
        // DECK A
        for(int i = 0; i < 4;  i++) {
            currentDeck.add(createTestCard(CardType.MONSTER, CardName.Elf, CardElement.FIRE, 1.0));
            assertNotNull(currentDeck.get(i));
            insertTestCard(playerB.getUserID() + i, currentDeck.get(i));
            cardHolderHandler.insertCardToHolder(playerB.getUserID(), playerB.getUserID() + i, false);
            cardIDs.add(playerB.getUserID() + i);
        }

        Deck deckB = DeckImpl.builder()
                .userID(playerB.getUserID())
                .deckList(currentDeck)
                .cardIDs(cardIDs)
                .build();
        deckHandler.setNewDeck(deckB.getCardIDs(),deckB.getUserID());

    }
    private ArrayList<Player> createTestPlayers(){
        ArrayList<Player> players = new ArrayList<>();
        PlayerHandler playerHandler = new PlayerHandlerImpl();
        Player playerA = getTestUser("A");
        Player playerB = getTestUser("B");
        createTestDecks(playerA,playerB);

        players.add(playerHandler.getItemById(playerA.getUserID()));
        players.add(playerHandler.getItemById(playerB.getUserID()));
        return players;
    }
    private Battle createTestBattle(){
        ArrayList<Player> players = createTestPlayers();
        assertNotNull(players.get(0));
        assertNotNull(players.get(1));
        return BattleImpl.builder()
                .player1(players.get(0))
                .player2(players.get(1))
                .searching(false)
                .build();
    }




    @Test
    void playGame() {
        Battle battle = createTestBattle();
        assertNotNull(battle.getPlayer1());
        assertNotNull(battle.getPlayer2());
        battle = insert(battle);
        battle =  battleHandler.playGame(battle);
        assertFalse(battle.isSearching());
        assertNotEquals(battle.getRound(),0);
        assertEquals(battle.getWinner().getUserID(),battle.getPlayer1().getUserID());
        delete(battle);
    }


    private Battle insert(Battle testBattle) {
        assertNotNull(testBattle);
        Battle battle = battleHandler.insert(testBattle);
        assertNotNull(battle);
        return battle;
    }
    private void delete(Battle battle) {
        PlayerHandler playerHandler = new PlayerHandlerImpl();
        CardHolderHandler cardHolderHandler = new CardHolderHandlerImpl();
        CardHandler cardHandler = new CardHandlerImpl();
        DeckHandler deckHandler = new DeckHandlerImpl();

        assertNotNull(battle);
        battleHandler.delete(battle);
        assertNotNull(battle.getPlayer1());
        assertNotNull(battle.getPlayer2());

        Player player1 = playerHandler.getItemById(battle.getPlayer1().getUserID());
        assertNotNull(player1);
        Player player2 = playerHandler.getItemById(battle.getPlayer2().getUserID());
        assertNotNull(player2);

        // DECK A
        Deck deckA = deckHandler.getItemById(player1.getUserID());
        Deck deckB = deckHandler.getItemById(player2.getUserID());
        assertNotNull(deckA);
        deckHandler.delete(deckA);
        assertNotNull(deckB);
        deckHandler.delete(deckB);

        for(Card card : deckA.getDeckList()){
            assertNotNull(card);
            cardHolderHandler.removeCardFromStack(player1.getUserID(),card.getCardID());
            cardHandler.delete(card);
        }

        for(Card card : deckB.getDeckList()){
            assertNotNull(card);
            cardHolderHandler.removeCardFromStack(player2.getUserID(),card.getCardID());
            cardHandler.delete(card);
        }


        playerHandler.delete(player1);
        playerHandler.delete(player2);
    }


}