package battle;

import at.technikum.logic.BattleLogicImpl;
import at.technikum.model.card.Card;
import at.technikum.model.card.cardTypes.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("BattleLogic")
class BattleLogicImplTest {

    @Mock
    BattleLogicImpl battleLogic = new BattleLogicImpl();

    @Test
    @DisplayName("Goblins are too afraid of Dragons to attack.")
    void testDragonVsGoblin() {
        Card dragon = MonsterCardImpl.builder()
                .cardName(CardName.Dragon)
                .cardTyp(CardType.MONSTER)
                .build();
        Card goblin = MonsterCardImpl.builder()
                .cardName(CardName.Goblin)
                .cardTyp(CardType.MONSTER)
                .build();

        // assert
        assertNotNull(dragon.getCardName());
        assertNotNull(goblin.getCardName());
        assertEquals(battleLogic.rules(goblin,dragon),dragon);
        assertEquals(battleLogic.rules(dragon,goblin),dragon);
    }

    @Test
    @DisplayName("Wizard can control Orks so they are not able to damage them.")
    void testWizardVsOrk() {
        Card wizard = MonsterCardImpl.builder()
                .cardName(CardName.Wizard)
                .cardTyp(CardType.MONSTER)
                .build();
        Card ork = MonsterCardImpl.builder()
                .cardName(CardName.Ork)
                .cardTyp(CardType.MONSTER)
                .build();

        // assert
        assertNotNull(wizard.getCardName());
        assertNotNull(ork.getCardName());
        assertEquals(battleLogic.rules(wizard,ork),wizard);
        assertEquals(battleLogic.rules(ork,wizard),wizard);
    }

    @Test
    @DisplayName("The Kraken is immune against spells.")
    void testKrakenVsSpell() {
        Card kraken = MonsterCardImpl.builder()
                .cardName(CardName.Kraken)
                .cardTyp(CardType.MONSTER)
                .build();
        Card spell = SpellCardImpl.builder()
                .cardTyp(CardType.SPELL)
                .build();

        // assert
        assertNotNull(kraken.getCardName());
        assertNotNull(spell.getCardType());
        assertEquals(battleLogic.rules(kraken,spell),kraken);
        assertEquals(battleLogic.rules(spell,kraken),kraken);
    }

    @Test
    @DisplayName("The FireElves know Dragons since they were little and can evade their attacks.")
    void testFireElfVsDragon() {
        Card fireElf = MonsterCardImpl.builder()
                .cardName(CardName.FireElf)
                .cardTyp(CardType.MONSTER)
                .build();
        Card dragon = MonsterCardImpl.builder()
                .cardName(CardName.Dragon)
                .cardTyp(CardType.MONSTER)
                .build();

        // assert
        assertNotNull(fireElf.getCardName());
        assertNotNull(dragon.getCardName());
        assertEquals(battleLogic.rules(fireElf,dragon),fireElf);
        assertEquals(battleLogic.rules(dragon,fireElf),fireElf);
    }

    @Test
    @DisplayName("The armor of Knights is so heavy that WaterSpells make them drown them instantly.")
    void testKnightVsWaterSpell() {
        Card knight = MonsterCardImpl.builder()
                .cardName(CardName.Knight)
                .cardTyp(CardType.MONSTER)
                .build();
        Card waterSpell = SpellCardImpl.builder()
                .cardName(CardName.WaterSpell)
                .cardTyp(CardType.MONSTER)
                .build();

        // assert
        assertNotNull(knight.getCardName());
        assertNotNull(waterSpell.getCardName());
        assertEquals(battleLogic.rules(knight,waterSpell),waterSpell);
        assertEquals(battleLogic.rules(waterSpell,knight),waterSpell);
    }


    @Test
    @DisplayName("water -> fire")
    void testWaterVsFire() {
        Card monsterWater = SpellCardImpl.builder()
                .cardElement(CardElement.WATER)
                .cardTyp(CardType.SPELL)
                .cardPower(10)
                .build();

        Card monsterFire = SpellCardImpl.builder()
                .cardElement(CardElement.FIRE)
                .cardTyp(CardType.SPELL)
                .cardPower(20)
                .build();

        Card spellWater = SpellCardImpl.builder()
                .cardElement(CardElement.WATER)
                .cardTyp(CardType.SPELL)
                .cardPower(20)
                .build();

        Card spellFire = SpellCardImpl.builder()
                .cardElement(CardElement.FIRE)
                .cardTyp(CardType.SPELL)
                .cardPower(10)
                .build();

        // assert
        assertNotNull(monsterWater.getCardElement());
        assertNotNull(monsterFire.getCardElement());
        assertNotNull(spellFire.getCardElement());
        assertNotNull(spellWater.getCardElement());

        assertEquals(battleLogic.rules(monsterWater,spellFire),monsterWater);
        assertEquals(battleLogic.rules(spellFire,monsterWater),monsterWater);
    }

    @Test
    @DisplayName("normal -> water")
    void testNormalVsWater() {
        Card monsterWater = SpellCardImpl.builder()
                .cardElement(CardElement.WATER)
                .cardTyp(CardType.SPELL)
                .cardPower(10)
                .build();

        Card monster = SpellCardImpl.builder()
                .cardElement(CardElement.NORMAL)
                .cardTyp(CardType.SPELL)
                .cardPower(20)
                .build();

        Card spellWater = SpellCardImpl.builder()
                .cardElement(CardElement.WATER)
                .cardTyp(CardType.SPELL)
                .cardPower(20)
                .build();

        Card spell = SpellCardImpl.builder()
                .cardElement(CardElement.NORMAL)
                .cardTyp(CardType.SPELL)
                .cardPower(10)
                .build();

        // assert
        assertNotNull(monster.getCardElement());
        assertNotNull(monsterWater.getCardElement());
        assertNotNull(spell.getCardElement());
        assertNotNull(spellWater.getCardElement());

        assertEquals(battleLogic.rules(monsterWater,monster),monster);
        assertEquals(battleLogic.rules(monster,monsterWater),monster);

        assertEquals(battleLogic.rules(spellWater,monster),monster);
        assertEquals(battleLogic.rules(monster,spellWater),monster);

        assertEquals(battleLogic.rules(monsterWater,spell),spell);
        assertEquals(battleLogic.rules(spell,monsterWater),spell);

        assertEquals(battleLogic.rules(spellWater,spell),spell);
        assertEquals(battleLogic.rules(spell,spellWater),spell);
    }

    @Test
    @DisplayName("normal -> fire")
    void testNormalVsFire() {
        Card monsterFire = SpellCardImpl.builder()
                .cardElement(CardElement.FIRE)
                .cardTyp(CardType.SPELL)
                .cardPower(10)
                .build();

        Card monster = SpellCardImpl.builder()
                .cardElement(CardElement.NORMAL)
                .cardTyp(CardType.SPELL)
                .cardPower(20)
                .build();

        Card spellFire = SpellCardImpl.builder()
                .cardElement(CardElement.FIRE)
                .cardTyp(CardType.SPELL)
                .cardPower(20)
                .build();

        Card spell = SpellCardImpl.builder()
                .cardElement(CardElement.NORMAL)
                .cardTyp(CardType.SPELL)
                .cardPower(10)
                .build();

        // assert
        assertNotNull(monster.getCardElement());
        assertNotNull(monsterFire.getCardElement());
        assertNotNull(spell.getCardElement());
        assertNotNull(spellFire.getCardElement());

        assertEquals(battleLogic.rules(monsterFire,monster),monsterFire);
        assertEquals(battleLogic.rules(monster,monsterFire),monsterFire);

        assertEquals(battleLogic.rules(spellFire,monster),spellFire);
        assertEquals(battleLogic.rules(monster,spellFire),spellFire);

        assertEquals(battleLogic.rules(monsterFire,spell),monsterFire);
        assertEquals(battleLogic.rules(spell,monsterFire),monsterFire);

        assertEquals(battleLogic.rules(spellFire,spell),spellFire);
        assertEquals(battleLogic.rules(spell,spellFire),spellFire);
    }

    @Test
    @DisplayName("powerA > power B ")
    void testPowerWinner() {
        Card goblinA = SpellCardImpl.builder()
                .cardName(CardName.Goblin)
                .cardTyp(CardType.MONSTER)
                .cardPower(200)
                .build();

        Card goblinB = SpellCardImpl.builder()
                .cardName(CardName.Goblin)
                .cardTyp(CardType.MONSTER)
                .cardPower(100)
                .build();



        // assert
        assertNotNull(goblinA.getCardName());
        assertNotNull(goblinA.getCardPower());

        assertNotNull(goblinB.getCardName());
        assertNotNull(goblinB.getCardPower());

        assertEquals(battleLogic.rules(goblinA,goblinB),goblinA);
        assertEquals(battleLogic.rules(goblinB,goblinA),goblinA);

    }

    @Test
    @DisplayName("powerA == power B ")
    void testPowerEqual() {
        Card goblinA = SpellCardImpl.builder()
                .cardName(CardName.Goblin)
                .cardTyp(CardType.MONSTER)
                .cardPower(100)
                .build();

        Card goblinB = SpellCardImpl.builder()
                .cardName(CardName.Goblin)
                .cardTyp(CardType.MONSTER)
                .cardPower(100)
                .build();



        // assert
        assertNotNull(goblinA.getCardName());
        assertNotNull(goblinA.getCardPower());

        assertNotNull(goblinB.getCardName());
        assertNotNull(goblinB.getCardPower());

        assertEquals(battleLogic.rules(goblinA,goblinB),null);
        assertEquals(battleLogic.rules(goblinB,goblinA),null);

    }

}