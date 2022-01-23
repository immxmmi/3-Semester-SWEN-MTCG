package battle;

import at.technikum.handler.BattleHandlerImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    private ArrayList<Deck> createTestDecks() {
        ArrayList<Card> currentDeck = new ArrayList<>();
        // DECK A
        currentDeck.add(createTestCard(CardType.MONSTER,CardName.Dragon,CardElement.FIRE,200.0));
        assertNotNull(currentDeck.get(0));
        currentDeck.add(createTestCard(CardType.MONSTER,CardName.Dragon,CardElement.FIRE,200.0));
        assertNotNull(currentDeck.get(1));
        currentDeck.add(createTestCard(CardType.MONSTER,CardName.Dragon,CardElement.FIRE,200.0));
        assertNotNull(currentDeck.get(2));
        currentDeck.add(createTestCard(CardType.MONSTER,CardName.Dragon,CardElement.FIRE,200.0));
        assertNotNull(currentDeck.get(3));

        Deck deckA = DeckImpl.builder()
                .userID("A")
                .deckList(currentDeck)
                .build();


        // DECK B
        currentDeck = new ArrayList<>();
        currentDeck.add(createTestCard(CardType.MONSTER,CardName.Goblin,CardElement.FIRE,10.0));
        assertNotNull(currentDeck.get(0));
        currentDeck.add(createTestCard(CardType.MONSTER,CardName.Goblin,CardElement.FIRE,10.0));
        assertNotNull(currentDeck.get(1));
        currentDeck.add(createTestCard(CardType.SPELL,CardName.Goblin,CardElement.FIRE,10.0));
        assertNotNull(currentDeck.get(2));
        currentDeck.add(createTestCard(CardType.MONSTER,CardName.Goblin,CardElement.FIRE,10.0));
        assertNotNull(currentDeck.get(3));

        Deck deckB = DeckImpl.builder()
                .userID("B")
                .deckList(currentDeck)
                .build();

        ArrayList<Deck> decks = new ArrayList<>();
        decks.add(deckA);
        decks.add(deckB);
        return decks;
    }
    private ArrayList<Player> createTestPlayers(){
        assertNotNull(createTestDecks());
        assertEquals(createTestDecks().size(),2);
        Deck deckA = createTestDecks().get(0);
        assertNotNull(deckA);
        Deck deckB = createTestDecks().get(1);
        assertNotNull(deckB);

        Player playerA = PlayerImpl.builder()
                .userID("A")
                .username("playerA")
                .deck(deckA)
                .elo(100.00)
                .build();

        Player playerB = PlayerImpl.builder()
                .userID("B")
                .username("playerB")
                .deck(deckB)
                .elo(100.00)
                .build();


        ArrayList<Player> players = new ArrayList<>();
        players.add(playerA);
        players.add(playerB);

        return players;
    }
    private Battle createTestBattle(){

        assertNotNull(createTestPlayers());
        assertEquals(createTestPlayers().size(),2);
        Player playerA = createTestPlayers().get(0);
        Player playerB = createTestPlayers().get(1);

        return BattleImpl.builder()
                .battleID("BattleA")
                .player1(playerA)
                .player2(playerB)
                .searching(false)
                .build();
    }


    @Test
    void playGame() {
        assertNotNull(createTestBattle().getPlayer1());
        assertNotNull(createTestBattle().getPlayer2());
        Battle battleEnd = this.battleHandler.playGame(createTestBattle());

    }


    @Test
    void insert() {
        Battle testBattle = createTestBattle();
        assertNotNull(testBattle);
        battleHandler.insert(testBattle);
        Battle dataBaseBattle = battleHandler.getItemById(testBattle.getBattleID());
        //assertNotNull(dataBaseBattle);
        //assertEquals(dataBaseBattle.getBattleID(),testBattle.getBattleID());
    }

    @Test
    void delete() {
    }
}