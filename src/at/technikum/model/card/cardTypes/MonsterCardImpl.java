package at.technikum.model.card.cardTypes;

import at.technikum.model.card.AbstractCardImpl;
import lombok.Builder;
import lombok.Getter;
import net.bytebuddy.implementation.bind.annotation.Default;


public class MonsterCardImpl extends AbstractCardImpl {

    /**
     * Goblins are too afraid of Dragons to attack.
     * • Wizzard can control Orks so they are not able to damage them.
     * • The armor of Knights is so heavy that WaterSpells make them drown them instantly.
     * • The Kraken is immune against spells.
     * • The FireElves know Dragons since they were little and can evade their attacks.
     */
    @Getter
    @Builder.Default
    CardType cardType = CardType.MONSTER;

    @Builder
    public MonsterCardImpl(String cardID, CardType cardTyp, CardName cardName, double cardPower, CardElement cardElement) {
        super(cardID, cardName, cardPower, cardElement);
        this.cardType = cardTyp;
    }

    public MonsterCardImpl() {
        this.cardType = CardType.MONSTER;
    }

}
