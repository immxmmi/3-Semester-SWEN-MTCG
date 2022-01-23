package at.technikum.test;

import at.technikum.model.card.Card;
import at.technikum.model.card.cardTypes.CardType;
import at.technikum.model.card.cardTypes.MonsterCardImpl;
import at.technikum.model.card.cardTypes.SpellCardImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpellCardImplTest {

    @Test
    @DisplayName("Spell")
    void getCardType() {
        Card spellCard = new SpellCardImpl();
        CardType cardType = spellCard.getCardType();

        // assert
        assertNotNull(cardType);
        assertEquals(CardType.SPELL, cardType);
    }
}