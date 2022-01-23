package card;

import at.technikum.model.card.Card;
import at.technikum.model.card.cardTypes.CardType;
import at.technikum.model.card.cardTypes.MonsterCardImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("MonsterCard")
class MonsterCardImplTest {

    @Mock
    Card card;

    @Test
    @DisplayName("Monster == CardTyp.MONSTER")
    void getCardType() {
        Card monsterCard = new MonsterCardImpl();
        CardType cardType = monsterCard.getCardType();

        // assert
        assertNotNull(cardType);
        assertEquals(CardType.MONSTER, cardType);
    }
}