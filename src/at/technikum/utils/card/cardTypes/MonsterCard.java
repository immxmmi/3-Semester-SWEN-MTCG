package at.technikum.utils.card.cardTypes;


import at.technikum.utils.card.AbstractCard;
import lombok.Builder;
import lombok.Getter;

public class MonsterCard extends AbstractCard {

    /**
     * Goblins are too afraid of Dragons to attack.
     * • Wizzard can control Orks so they are not able to damage them.
     * • The armor of Knights is so heavy that WaterSpells make them drown them instantly.
     * • The Kraken is immune against spells.
     * • The FireElves know Dragons since they were little and can evade their attacks.
     */
    @Getter
    CardType cardType;

    @Builder
    public MonsterCard(String cardID, CardType cardTyp, CardName cardName, double cardPower, CardElement cardElement) {
        super(cardID, cardName, cardPower, cardElement);
        this.cardType = cardTyp;
    }

}
