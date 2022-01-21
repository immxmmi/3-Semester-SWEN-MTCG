package at.technikum.services;


import at.technikum.model.repository.Battle;
import at.technikum.repository.PlayerRepository;
import at.technikum.repository.PlayerRepositoryImpl;
import at.technikum.utils.card.ICard;
import at.technikum.utils.card.cardTypes.CardElement;
import at.technikum.utils.card.cardTypes.CardName;
import at.technikum.utils.card.cardTypes.CardType;
import at.technikum.utils.logger.LoggerStatic;
import at.technikum.utils.tools.TextColor;
import at.technikum.utils.tools.Tools;

import java.util.Collections;
import java.util.List;

public class BattleLogicImpl extends Tools implements BattleLogic {

    LoggerStatic loggerStatic = LoggerStatic.getInstance();
    PlayerRepository playerService = new PlayerRepositoryImpl();


    @Override
    public Battle start(Battle battle){
        return this.fight(battle);
    }


    private Battle fight(Battle battle) {
        List<ICard> deckA = battle.getPlayer1().getDeck().getDeckList();
        List<ICard> deckB = battle.getPlayer2().getDeck().getDeckList();
        Collections.shuffle(deckA);
        Collections.shuffle(deckB);
        ICard cardA;
        ICard cardB;
        int randomA;
        int randomB;
        int round = 0;

        do{
            loggerStatic.log("ROUND:" + round);
            System.out.println("ROUND: " + round);
            System.out.println(deckA.size());
            System.out.println(deckB.size());
            randomA = randomNumber(0, deckA.size());
            randomB = randomNumber(0, deckB.size());
            cardA = deckA.get(randomA);
            cardB = deckB.get(randomB);
            this.round(cardA,cardB);
            ICard WinnerCard = this.rules(cardA,cardB);

            if (WinnerCard == cardA) {
              //  System.out.println(TextColor.ANSI_GREEN + "Winner is Player A" + TextColor.ANSI_RESET);
                loggerStatic.log("Winner is Player A");
                deckA.add(cardB);
                deckB.remove(cardB);
            }

            if (WinnerCard == cardB) {
                //System.out.println(TextColor.ANSI_GREEN + "Winner is Player B" + TextColor.ANSI_RESET);
                loggerStatic.log("Winner is Player B");
                deckB.add(cardA);
                deckA.remove(cardA);
            }

            if (WinnerCard == null) {
             //   System.out.println(TextColor.ANSI_WHITE + "The game is a draw" + TextColor.ANSI_RESET);
                loggerStatic.log("The game is a draw");
            }
            if (deckA.size() < 1){
                break;
            }
            if(deckB.size() < 1){
                break;
            }
            round ++;
        }while(round < 100);

        System.out.println("\n" + TextColor.ANSI_PURPLE + "WINNER : ");


        battle.setRound(round);

        if(deckA.size() > deckB.size()){
            battle.setWinner(battle.getPlayer1());
            battle.getPlayer1().setElo(battle.getPlayer1().getElo()+3);
            battle.getPlayer2().setElo(battle.getPlayer2().getElo()-5);
        }

        if(deckA.size() < deckB.size()){
            battle.setWinner(battle.getPlayer2());
            battle.getPlayer2().setElo(battle.getPlayer2().getElo()+3);
            battle.getPlayer1().setElo(battle.getPlayer1().getElo()-5);
        }


        playerService.update(battle.getPlayer1());
        playerService.update(battle.getPlayer2());
        return battle;
    }


    private void round(ICard CardA, ICard CardB) {
        System.out.print(TextColor.ANSI_BLUE + "PlayerA: ");
        System.out.print(CardA.getCardName() + "(" + CardA.getCardPower() + ")");
        System.out.print(" vs ");
        System.out.print("PlayerB: ");
        System.out.print(CardB.getCardName() + "(" + CardB.getCardPower() + ")");
        System.out.println(TextColor.ANSI_RESET);
    }

    private ICard rules(ICard CardA, ICard CardB) {


        if(CardA.getCardName() == CardName.Dragon && CardB.getCardName() == CardName.Goblin){
            System.out.println("Goblins are too afraid of Dragons to attack.");
            loggerStatic.log("Goblins are too afraid of Dragons to attack.");
            return CardA;
        }
        if(CardB.getCardName() == CardName.Dragon && CardA.getCardName() == CardName.Goblin){
            System.out.println("Goblins are too afraid of Dragons to attack.");
            loggerStatic.log("Goblins are too afraid of Dragons to attack.");
            return CardB;
        }

        if(CardA.getCardName() == CardName.Wizzard && CardB.getCardName() == CardName.Ork){
            System.out.println("Wizzard can control Orks so they are not able to damage them.");
            loggerStatic.log("Wizzard can control Orks so they are not able to damage them.");
            return CardA;
        }
        if(CardB.getCardName() == CardName.Wizzard && CardA.getCardName() == CardName.Ork){
            System.out.println("Wizzard can control Orks so they are not able to damage them.");
            loggerStatic.log("Wizzard can control Orks so they are not able to damage them.");
            return CardB;
        }

        if(CardA.getCardName() == CardName.Kraken && CardB.getCardType() == CardType.SPELL){
            System.out.println("The Kraken is immune against spells.");
            loggerStatic.log("The Kraken is immune against spells.");
            return CardA;
        }
        if(CardB.getCardName() == CardName.Kraken && CardA.getCardType() == CardType.SPELL){
            System.out.println("The Kraken is immune against spells.");
            loggerStatic.log("The Kraken is immune against spells.");
            return CardB;
        }

        if(CardA.getCardName() == CardName.FireElf && CardB.getCardName() == CardName.Dragon){
            System.out.println("The FireElves know Dragons since they were little and can evade their attacks.");
            loggerStatic.log("The FireElves know Dragons since they were little and can evade their attacks.");
            return CardA;
        }
        if(CardB.getCardName() == CardName.FireElf && CardA.getCardName() == CardName.Dragon){
            System.out.println("The FireElves know Dragons since they were little and can evade their attacks.");
            loggerStatic.log("The FireElves know Dragons since they were little and can evade their attacks.");
            return CardB;
        }

        if(CardA.getCardName() == CardName.WaterSpell && CardB.getCardName() == CardName.Knight){
            System.out.println("The armor of Knights is so heavy that WaterSpells make them drown them instantly.");
            loggerStatic.log("The armor of Knights is so heavy that WaterSpells make them drown them instantly.");
            return CardA;
        }
        if(CardB.getCardName() == CardName.WaterSpell && CardA.getCardName() == CardName.Knight){
            System.out.println("The armor of Knights is so heavy that WaterSpells make them drown them instantly.");
            loggerStatic.log("The armor of Knights is so heavy that WaterSpells make them drown them instantly.");
            return CardB;
        }

        if(CardA.getCardElement() == CardElement.WATER && CardB.getCardElement() == CardElement.FIRE){
            System.out.println("water -> fire");
            loggerStatic.log("water -> fire");
            if((CardA.getCardPower()*2) > (CardB.getCardPower()/2)){
                return CardA;
            }
            if((CardA.getCardPower()*2) < (CardB.getCardPower()/2)){
                return CardB;
            }
            return null;
        }
        if(CardB.getCardElement() == CardElement.WATER && CardA.getCardElement() == CardElement.FIRE){
            System.out.println("water -> fire");
            loggerStatic.log("water -> fire");
            if((CardB.getCardPower()*2) > (CardA.getCardPower()/2)){
                return CardB;
            }
            if((CardB.getCardPower()*2) < (CardA.getCardPower()/2)){
                return CardA;
            }
            return null;
        }

        if(CardA.getCardElement() == CardElement.NORMAL && CardB.getCardElement() == CardElement.WATER){
            System.out.println("normal -> water");
            loggerStatic.log("normal -> water");
            return CardA;
        }
        if(CardB.getCardElement() == CardElement.NORMAL && CardA.getCardElement() == CardElement.WATER){
            System.out.println("normal -> water");
            loggerStatic.log("normal -> water");
            return CardB;
        }


        if(CardA.getCardElement() == CardElement.FIRE && CardB.getCardElement() == CardElement.NORMAL){
            System.out.println("fire -> normal");
            loggerStatic.log("fire -> normal");
            return CardA;
        }
        if(CardB.getCardElement() == CardElement.FIRE && CardA.getCardElement() == CardElement.NORMAL){
            System.out.println("fire -> normal");
            loggerStatic.log("fire -> normal");
            return CardB;
        }


        if (CardA.getCardPower() == CardB.getCardPower()) {
            return null;
        }

        if (CardA.getCardPower() > CardB.getCardPower()) {
            return CardA;
        }

        if (CardA.getCardPower() < CardB.getCardPower()) {
            return CardB;
        }


    return null;
    }



}
