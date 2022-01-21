package at.technikum.services;

import at.technikum.model.repository.Player;
import at.technikum.model.repository.Store;
import at.technikum.handler.repository.PlayerHandler;
import at.technikum.handler.PlayerHandlerImpl;
import at.technikum.utils.Printer;
import at.technikum.utils.PrinterImpl;
import at.technikum.handler.CardHandler;
import at.technikum.handler.CardHolderHandlerImpl;
import at.technikum.handler.repository.CardHolderHandler;
import at.technikum.handler.repository.StackHandler;
import at.technikum.handler.StackHandlerImpl;
import at.technikum.handler.repository.StoreHandler;

import java.util.ArrayList;
import java.util.Scanner;

public class StoreServices {

    Player currentPlayer;
    PlayerHandler playerHandler;
    StoreHandler storeHandler;
    Printer printer;

    public StoreServices(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.printer = new PrinterImpl();
    }

    /*******************************************************************/
    /**                           SERVICE                             **/
    /*******************************************************************/
    public Player StoreMenu() {

        if (this.currentPlayer == null) {
            System.out.println("NO currentPlayer -- LOGIN");
            return null;
        }
        System.out.println("####################");
        System.out.println(" --  STORE MENU  -- ");
        System.out.println("####################");
        System.out.println("#   1. Buy         #");
        System.out.println("#   2. Sell        #");
        System.out.println("#   3. Setting     #");
        System.out.println("####################");


        switch (getIndex(3)) {
            case 1:
                this.storeListMenu();
                break;
            case 2:
                this.sellMenu();
                break;
            case 3:
                this.itemSetting();
                break;
        }


        return this.currentPlayer;
    }

    private void sellMenu() {

        int price = 0;
        int index;
        int size = (currentPlayer.getFreeStack().getStack().size()) - 1;
        String cardID;
        String userID = currentPlayer.getUserID();
        CardHolderHandler holder = new CardHolderHandlerImpl();
        StackHandler stackService = new StackHandlerImpl();


        System.out.println("|#################################################|");
        System.out.println("|##############   STORE -- SELLER   ##############|");
        System.out.println("|#################################################|");


        printer.printStack(this.currentPlayer.getFreeStack());


        System.out.println("CHOOSE CARD");

        index = getIndex(size);

        cardID = this.currentPlayer.getFreeStack().getStack().get(index).getCardID();


        price = storeHandler.getPrice();
        storeHandler.addItemToStore(cardID, price);
        holder.updateLocked(holder.getCardHolder(userID, cardID), true);
        this.currentPlayer = playerHandler.reloadAccount(this.currentPlayer);
    }

    private void transactionSettingsMenu(String transactionID, String sellerID, String itemID) {

        System.out.println("####################");
        System.out.println(" --  ITEM MENU   -- ");
        System.out.println("####################");
        System.out.println("#   1. PRICE       #");
        System.out.println("#   2. DELETE      #");
        System.out.println("#   0. BACK        #");
        System.out.println("####################");

        switch (getIndex(3)) {
            case 0:
                this.itemSetting();
                break;
            case 1:
                this.storeHandler.changePrice(transactionID);
                break;
            case 2:
                this.storeHandler.removeTransaction(transactionID, sellerID, itemID);
                break;
        }

    }

    private void storeListMenu() {
        System.out.println("##############################     -STORE-     ##############################");
        System.out.println("##| count | SELLER              | ITEM              | PRICE | DATE     | ## #");
        System.out.println("#############################################################################");

        PlayerHandlerImpl seller = new PlayerHandlerImpl();
        CardHandler card = new CardHandler();
        int count = 0;

        for (Store i : storeHandler.getAllTransaction()) {
            if (i.getSellerID().equals(storeHandler.getStoreID())) {

                System.out.println(
                        "##|   " + count +
                                "   | Store               " +
                                "| -PACKAGE           " +
                                "|  " + i.getPrice() + "   " +
                                //  "|"+i.getTimeStamp()+"      |"
                                " | DATE     |" +
                                " ## #"

                );

            } else {

                System.out.println(
                        "##|" + count +
                                "|" + seller.getItemById(i.getSellerID()).getUsername() + "               " +
                                "|" + card.getCardById(i.getItemID()).getCardName() + "               " +
                                "|   " + i.getPrice() + "   " +
                                //"|"+i.getTimeStamp()+"      |"
                                "|  DATE    |" +
                                " ##"
                );
            }

            count++;
        }
        System.out.println("#############################################################################");


        int storeSize = storeHandler.getAllTransaction().size();


        // INPUT
        Scanner input = new Scanner(System.in);
        // ITEM - MENU
        int trans;


        do {
            trans = input.nextInt();
        } while (trans < 0 && trans > storeSize);

        storeHandler.buyItem(storeHandler.getAllTransaction().get(trans));
    }

    // SERVICE
    private void itemSetting() {

        String sellerID = currentPlayer.getUserID();
        ArrayList<Store> transactions = storeHandler.getAllTransactionByUserID(sellerID);
        CardHandler card = new CardHandler();
        int index;
        int number = 0;
        int size = (transactions.size() - 1);
        String transactionID;
        String itemID;


        System.out.println("|#################################################|");
        System.out.println("|############   ITEM -- SETTINGS   ##############|");
        System.out.println("|#################################################|");

        for (Store transaction : transactions) {
            System.out.println("-----------------------------------------------------");
            System.out.println("# number " + number);
            System.out.println(transaction.getTransactionID());
            System.out.println(card.getCardById(transaction.getItemID()).getCardName());
            System.out.println(transaction.getPrice());
            System.out.println("-----------------------------------------------------");
            number++;
        }

        System.out.println("CHOOSE Transaction");
        index = getIndex(size);

        transactionID = transactions.get(index).getTransactionID();
        itemID = transactions.get(index).getItemID();
        transactionSettingsMenu(transactionID, sellerID, itemID);

        this.currentPlayer = playerHandler.reloadAccount(currentPlayer);
    }
    private int getIndex(int size) {
        int index = 0;
        Scanner input = new Scanner(System.in);
        do {
            index = input.nextInt();
            if (index > size || index < 0) {
                System.out.println("ERROR - TRY AGAIN");
            }
        } while (index > size || index < 0);

        return index;
    }

    /*******************************************************************/

}
