package at.technikum.model.card.cardTypes;

import at.technikum.model.card.AbstractCardImpl;
import lombok.Builder;
import lombok.Getter;

public class SpellCardImpl extends AbstractCardImpl {

    /**
     * • water -> fire
     * • fire -> normal
     * • normal -> water
     * <p>
     * effective (eg: water is effective against fire, so damage is doubled)
     * – not effective (eg: fire is not effective against water, so damage is halved)
     * – no effect (eg: normal monster vs normal spell, no change of damage, direct
     **/

    @Getter
    CardType cardType;

    @Builder
    public SpellCardImpl(String cardID, CardType cardTyp, CardName cardName, double cardPower, CardElement cardElement) {
        super(cardID, cardName, cardPower, cardElement);
        this.cardType = cardTyp;
    }

    public SpellCardImpl(){
        this.cardType = CardType.SPELL;
    }
}
