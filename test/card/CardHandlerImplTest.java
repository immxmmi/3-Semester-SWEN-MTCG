package card;

import at.technikum.handler.CardHandlerImpl;
import at.technikum.handler.repository.CardHandler;
import at.technikum.model.card.Card;
import at.technikum.model.card.cardTypes.CardElement;
import at.technikum.model.card.cardTypes.CardName;
import at.technikum.model.card.cardTypes.CardType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CardHandler")
class CardHandlerImplTest {

    @Mock
    CardHandler cardHandler = new CardHandlerImpl();

    private Card createTestCard(){
        Card card = cardHandler.addCardByData("TEST",CardName.Dragon,CardType.MONSTER,CardElement.NORMAL,200.00);
        assertNotNull(card);
        card = cardHandler.getCardById(card.getCardID());
        assertNotNull(card);
        return card;
    }


    @Test
    void cardBuilder() {
        Card card = cardHandler.cardBuilder("TEST",CardName.Dragon.toString(),CardType.MONSTER, CardElement.NORMAL.toString(),"200.00");
        assertNotNull(card);
    }

    @Test
    void getAllCardsList() {
        Card card = createTestCard();
        assertNotNull(cardHandler.getAllCardsList());
        delete(card);
    }

    @Test
    void getCardById() {
        Card card = createTestCard();
        assertNotNull(cardHandler.getCardById(card.getCardID()));
        delete(card);
    }

    @Test
    void getCardByName() {
        Card card = createTestCard();
        assertNotNull(cardHandler.getCardByName(card.getCardName().toString()));
        delete(card);
    }


    @Test
    void filterCardType() {

        assertEquals(cardHandler.filterCardType(CardName.Spell),CardType.SPELL);
        assertEquals(cardHandler.filterCardType(CardName.RegularSpell),CardType.SPELL);
        assertEquals(cardHandler.filterCardType(CardName.FireSpell),CardType.SPELL);
        assertEquals(cardHandler.filterCardType(CardName.WaterSpell),CardType.SPELL);

        assertEquals(cardHandler.filterCardType(CardName.Goblin),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.Wizard),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.Dragon),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.Ork),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.Knight),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.Kraken),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.Elf),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.FireGoblin),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.FireDragon),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.FireWizzard),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.FireOrk),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.FireKnight),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.FireKraken),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.FireElf),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.WaterGoblin),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.WaterDragon),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.WaterWizard),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.WaterOrk),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.WaterKnight),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.WaterKraken),CardType.MONSTER);
        assertEquals(cardHandler.filterCardType(CardName.WaterElf),CardType.MONSTER);

    }

    @Test
    void filterCardElement() {
        assertEquals(cardHandler.filterCardElement(CardName.Spell),CardElement.NORMAL);
        assertEquals(cardHandler.filterCardElement(CardName.RegularSpell),CardElement.NORMAL);
        assertEquals(cardHandler.filterCardElement(CardName.FireSpell),CardElement.FIRE);
        assertEquals(cardHandler.filterCardElement(CardName.WaterSpell),CardElement.WATER);

        assertEquals(cardHandler.filterCardElement(CardName.Goblin),CardElement.NORMAL);
        assertEquals(cardHandler.filterCardElement(CardName.Wizard),CardElement.NORMAL);
        assertEquals(cardHandler.filterCardElement(CardName.Dragon),CardElement.NORMAL);
        assertEquals(cardHandler.filterCardElement(CardName.Ork),CardElement.NORMAL);
        assertEquals(cardHandler.filterCardElement(CardName.Knight),CardElement.NORMAL);
        assertEquals(cardHandler.filterCardElement(CardName.Kraken),CardElement.NORMAL);
        assertEquals(cardHandler.filterCardElement(CardName.Elf),CardElement.NORMAL);

        assertEquals(cardHandler.filterCardElement(CardName.FireGoblin),CardElement.FIRE);
        assertEquals(cardHandler.filterCardElement(CardName.FireDragon),CardElement.FIRE);
        assertEquals(cardHandler.filterCardElement(CardName.FireWizzard),CardElement.FIRE);
        assertEquals(cardHandler.filterCardElement(CardName.FireOrk),CardElement.FIRE);
        assertEquals(cardHandler.filterCardElement(CardName.FireKnight),CardElement.FIRE);
        assertEquals(cardHandler.filterCardElement(CardName.FireKraken),CardElement.FIRE);
        assertEquals(cardHandler.filterCardElement(CardName.FireElf),CardElement.FIRE);

        assertEquals(cardHandler.filterCardElement(CardName.WaterGoblin),CardElement.WATER);
        assertEquals(cardHandler.filterCardElement(CardName.WaterDragon),CardElement.WATER);
        assertEquals(cardHandler.filterCardElement(CardName.WaterWizard),CardElement.WATER);
        assertEquals(cardHandler.filterCardElement(CardName.WaterOrk),CardElement.WATER);
        assertEquals(cardHandler.filterCardElement(CardName.WaterKnight),CardElement.WATER);
        assertEquals(cardHandler.filterCardElement(CardName.WaterKraken),CardElement.WATER);
        assertEquals(cardHandler.filterCardElement(CardName.WaterElf),CardElement.WATER);
    }

    @Test
    void addCardByData() {
        Card card = createTestCard();
        delete(card);
    }


    private void delete(Card card){
        // DELETE
        assertNotNull(cardHandler.getCardById(card.getCardID()));
        cardHandler.delete(card);
        assertEquals(cardHandler.getCardById(card.getCardID()),null);
    }


}