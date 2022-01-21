package at.technikum.services;

import at.technikum.model.repository.Player;
import at.technikum.model.repository.Store;
import at.technikum.repository.PlayerRepository;
import at.technikum.repository.PlayerRepositoryImpl;
import at.technikum.utils.Printer;
import at.technikum.utils.PrinterImpl;
import at.technikum.utils.card.service.CardServices;
import at.technikum.repository.CardHolderRepositoryImpl;
import at.technikum.repository.CardHolderRepository;
import at.technikum.repository.StackRepository;
import at.technikum.repository.StackRepositoryImpl;
import at.technikum.repository.StoreRepository;

import java.util.ArrayList;
import java.util.Scanner;

public class StoreServices {

    Player currentPlayer;
    PlayerRepository playerRepository;
    StoreRepository storeRepository;
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
        CardHolderRepository holder = new CardHolderRepositoryImpl();
        StackRepository stackService = new StackRepositoryImpl();


        System.out.println("|#################################################|");
        System.out.println("|##############   STORE -- SELLER   ##############|");
        System.out.println("|#################################################|");


        printer.printStack(this.currentPlayer.getFreeStack());


        System.out.println("CHOOSE CARD");

        index = getIndex(size);

        cardID = this.currentPlayer.getFreeStack().getStack().get(index).getCardID();


        price = storeRepository.getPrice();
        storeRepository.addItemToStore(cardID, price);
        holder.updateLocked(holder.getCardHolder(userID, cardID), true);
        this.currentPlayer = playerRepository.reloadAccount(this.currentPlayer);
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
                this.storeRepository.changePrice(transactionID);
                break;
            case 2:
                this.storeRepository.removeTransaction(transactionID, sellerID, itemID);
                break;
        }

    }

    private void storeListMenu() {
        System.out.println("##############################     -STORE-     ##############################");
        System.out.println("##| count | SELLER              | ITEM              | PRICE | DATE     | ## #");
        System.out.println("#############################################################################");

        PlayerRepositoryImpl seller = new PlayerRepositoryImpl();
        CardServices card = new CardServices();
        int count = 0;

        for (Store i : storeRepository.getAllTransaction()) {
            if (i.getSellerID().equals(storeRepository.getStoreID())) {

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


        int storeSize = storeRepository.getAllTransaction().size();


        // INPUT
        Scanner input = new Scanner(System.in);
        // ITEM - MENU
        int trans;


        do {
            trans = input.nextInt();
        } while (trans < 0 && trans > storeSize);

        storeRepository.buyItem(storeRepository.getAllTransaction().get(trans));
    }

    // SERVICE
    private void itemSetting() {

        String sellerID = currentPlayer.getUserID();
        ArrayList<Store> transactions = storeRepository.getAllTransactionByUserID(sellerID);
        CardServices card = new CardServices();
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

        this.currentPlayer = playerRepository.reloadAccount(currentPlayer);
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
