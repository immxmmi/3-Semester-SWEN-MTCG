package card;

import at.technikum.model.card.Card;
import at.technikum.model.card.cardTypes.CardType;
import at.technikum.model.card.cardTypes.SpellCardImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("SpellCard")
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