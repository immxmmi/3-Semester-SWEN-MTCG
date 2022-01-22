package at.technikum.utils;

import at.technikum.logger.LoggerStatic;
import at.technikum.model.repository.Deck;
import at.technikum.model.repository.Player;
import at.technikum.model.repository.Profil;
import at.technikum.model.repository.Stack;
import at.technikum.model.card.Card;
import at.technikum.model.card.cardTypes.CardElement;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Printer {


    Tools tools;
    LoggerStatic loggerStatic;
    public Printer(){
        this.tools = new Tools();
        this.loggerStatic = LoggerStatic.getInstance();
    }


    /*** --> PRINT STACK**/
    public void printStack(Stack currentStack) {

        if (currentStack == null) {
            //System.out.println(TextColor.ANSI_RED + "NO STACK" + TextColor.ANSI_RESET);
            loggerStatic.log("\nNO STACK\n");
        }
        printCards(currentStack.getStack(), "stack");
    }
    public void printHighscoreList(LinkedHashMap<String, Double> highscores){
        if (highscores == null) {
            System.out.println(TextColor.ANSI_RED + "NO LIST" + TextColor.ANSI_RESET);
            return;
        }

        int counter = 1;
        System.out.println("###############################################");
        System.out.println("#                 HighScore                   #");
        System.out.println("###############################################");
        System.out.println("#                                             #");
         System.out.println("#       "+ counter +" "+highscores.entrySet());
        System.out.println("#                                             #");
        System.out.println("###############################################");
    }
    private void printCards(ArrayList<Card> stack, String name) {
        int maxNumber = 2;
        int maxType = 8;
        int maxName = 12;
        int maxElement = 8;
        int maxPower = 4;
        int maxH1 = 5;
        int number = 0;
        if (name.equals("")) {
            name = "STACK";
        }
        System.out.println("|#################################################|");
        System.out.println("|#                     " + name.toUpperCase() + tools.checkSpace(name, maxH1) + "                    #|");
        System.out.println("|#################################################|");
        System.out.println("| # " + tools.checkSpace("# ", maxNumber) + "| TYPE " + tools.checkSpace("TYPE ", maxType) + "| NAME " + tools.checkSpace("NAME ", maxName) + "| ELEMENT " + tools.checkSpace("ELEMENT ", maxElement) + "| POWER |");


        for (Card card : stack) {

            if (card.getCardElement() == CardElement.WATER) {
                System.out.print(TextColor.ANSI_CYAN);
            }
            if (card.getCardElement() == CardElement.FIRE) {
                System.out.print(TextColor.ANSI_RED);
            }
            if (card.getCardElement() == CardElement.NORMAL) {
                System.out.print(TextColor.ANSI_RESET);
            }

            System.out.println(
                    "| " + number + tools.checkSpace("" + number, maxNumber) +
                            "| " + card.getCardType() + tools.checkSpace(card.getCardType().toString(), maxType) +
                            "| " + card.getCardName() + tools.checkSpace(card.getCardName().toString(), maxName) +
                            "| " + card.getCardElement() + tools.checkSpace(card.getCardElement().toString(), maxElement) +
                            "| " + card.getCardPower() + tools.checkSpace("" + card.getCardPower(), maxPower) +
                            " |"
            );
            System.out.print(TextColor.ANSI_RESET);
            number++;
        }

        System.out.println("|#################################################|");

    }
    public void printPlayerInfo(Profil playerInfo) {
        if (playerInfo == null) {
            System.out.println(TextColor.ANSI_RED + "NO PLAYER INFO" + TextColor.ANSI_RESET);
            return;
        }
        int maxName = 31;
        int maxBio = 32;
        int maxImg = 30;
        System.out.println("###############################################");
        System.out.println("#                 PLAYER INFO                 #");
        System.out.println("###############################################");
        System.out.println("#                                             #");
        System.out.println("#       NAME: " + TextColor.ANSI_BLUE + playerInfo.getName() + tools.checkSpace(playerInfo.getName(), maxName) + TextColor.ANSI_RESET + "#");
        System.out.println("#       BIO: " + TextColor.ANSI_BLUE + playerInfo.getBio() +tools. checkSpace(playerInfo.getBio(), maxBio) + TextColor.ANSI_RESET + "#");
        System.out.println("#       IMAGE: " + TextColor.ANSI_BLUE + playerInfo.getImage() + tools.checkSpace(playerInfo.getImage(), maxImg) + TextColor.ANSI_RESET + "#");
        System.out.println("#                                             #");
        System.out.println("###############################################");
    }
    public void printDeck(Deck currentDeck) {

        if (currentDeck == null) {
            System.out.println(TextColor.ANSI_RED + "NO DECK" + TextColor.ANSI_RESET);
        }
        printCards((ArrayList) currentDeck.getDeckList(), "deck");
    }
    public void printPlayerData(Player currentPlayer) {
        int maxName = 27;
        int maxCoins = 30;
        int maxElo = 32;
        int maxStackSize = 25;
        System.out.println("###############################################");
        System.out.println("#                  USER DATA                  #");
        System.out.println("###############################################");
        System.out.println("#                                             #");
        System.out.println("#       Username: " + TextColor.ANSI_BLUE + currentPlayer.getUsername() + tools.checkSpace(currentPlayer.getUsername(), maxName) + TextColor.ANSI_RESET + "#");
        System.out.println("#       Coins: " + TextColor.ANSI_BLUE + currentPlayer.getCoins() + tools.checkSpace("" + currentPlayer.getCoins(), maxCoins) + TextColor.ANSI_RESET + "#");
        System.out.println("#       ELO: " + TextColor.ANSI_BLUE + currentPlayer.getElo() + tools.checkSpace("" + currentPlayer.getElo(), maxElo) + TextColor.ANSI_RESET + "#");
        System.out.println("#       Stack Size: " + TextColor.ANSI_BLUE + currentPlayer.getStack().getStack().size() + tools.checkSpace("" + currentPlayer.getStack().getStack().size(), maxStackSize) + TextColor.ANSI_RESET + "#");
        System.out.println("#                                             #");
        System.out.println("###############################################");
    }
}
