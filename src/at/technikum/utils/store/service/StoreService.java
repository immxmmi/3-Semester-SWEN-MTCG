package at.technikum.utils.store.service;

import at.technikum.database.AbstractDBTable;
import at.technikum.utils.card.service.CardServices;
import at.technikum.utils.cardHolder.service.CardHolderServices;
import at.technikum.utils.cardHolder.service.ICardHolderServices;
import at.technikum.utils.player.IPlayer;
import at.technikum.utils.player.service.PlayerService;
import at.technikum.utils.stack.service.IStackService;
import at.technikum.utils.stack.service.StackService;
import at.technikum.database.model.IStore;
import at.technikum.database.model.Store;
import at.technikum.utils.tools.TextColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class StoreService extends AbstractDBTable implements IStoreService {
    final static String storeID = "STORE - ST1120";
    IPlayer currentPlayer;
    PlayerService playerService = new PlayerService();


    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public StoreService(IPlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.tableName = "store";
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                            BUILDER                            **/
    /*******************************************************************/
    private IStore transactionBuilder(ResultSet result) {

        try {
            if (result.next()) {
                IStore item = Store.builder()
                        .transactionID(result.getString("transaction_id"))
                        .sellerID(result.getString("seller_id"))
                        .itemID(result.getString("item_id"))
                        .price(convertToDouble(result.getString("price")))
                        .timeStamp(result.getString("date"))
                        .build();

                this.closeStatement();

                return item;
            }
        } catch (SQLException e) {

            System.out.println("GETOBJECT -ERRROR: " + e);
            e.printStackTrace();
        }
        this.closeStatement();
        return null;
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                              ADD                              **/
    /*******************************************************************/
    private void addItemToStore(String itemID, int price) {

        ICardHolderServices holder = new CardHolderServices();

        holder.updateLocked(holder.getCardHolder(this.currentPlayer.getUserID(), itemID), true);

        //ICardHolder holder1 = holder.get
        //holder.updateLocked();

        addItemToStore(this.currentPlayer.getUserID(), itemID, price);
    }

    private void addItemToStore(String sellerID, String itemID, double price) {
        String transactionID = this.tokenSupplier.get();

        this.parameter = new String[]{
                "T - " + transactionID,
                sellerID,
                itemID,
                "" + price,
                "" + formatDate(2)
        };

        this.setStatement(
                "INSERT INTO store (transaction_id, seller_id, item_id, price , \"date\") values ( ?,?,?,?,?)",
                this.parameter
        );

    }

    @Override
    public void addPackageToStore(String packageID, double price) {
        addItemToStore(this.storeID, packageID, price);
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                              MENU                             **/
    /*******************************************************************/
    public IPlayer StoreMenu() {

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
        ICardHolderServices holder = new CardHolderServices();
        IStackService stackService = new StackService();


        System.out.println("|#################################################|");
        System.out.println("|##############   STORE -- SELLER   ##############|");
        System.out.println("|#################################################|");


        stackService.printStack(this.currentPlayer.getFreeStack());


        System.out.println("CHOOSE CARD");

        index = getIndex(size);

        cardID = this.currentPlayer.getFreeStack().getStack().get(index).getCardID();


        price = getPrice();
        addItemToStore(cardID, price);
        holder.updateLocked(holder.getCardHolder(userID, cardID), true);
        this.currentPlayer = playerService.reloadAccount(this.currentPlayer);
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
                this.changePrice(transactionID);
                break;
            case 2:
                this.removeTransaction(transactionID, sellerID, itemID);
                break;
        }

    }

    private void storeListMenu() {
        System.out.println("##############################     -STORE-     ##############################");
        System.out.println("##| count | SELLER              | ITEM              | PRICE | DATE     | ## #");
        System.out.println("#############################################################################");

        PlayerService seller = new PlayerService();
        CardServices card = new CardServices();
        int count = 0;

        for (IStore i : getAllTransaction()) {
            if (i.getSellerID().equals(storeID)) {

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
                                "|" + seller.getPlayerById(i.getSellerID()).getUsername() + "               " +
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


        int storeSize = getAllTransaction().size();


        // INPUT
        Scanner input = new Scanner(System.in);
        // ITEM - MENU
        int trans;


        do {
            trans = input.nextInt();
        } while (trans < 0 && trans > storeSize);

        buyItem(getAllTransaction().get(trans));
    }

    /*******************************************************************/


    /*******************************************************************/
    /**                            ACTION                             **/
    /*******************************************************************/

    public IPlayer sellPackage(String customerID, String packageID) {
        PlayerService playerService = new PlayerService();
        this.currentPlayer = playerService.getPlayerById(customerID);
        buyItem(getItemByItemID(packageID));
        return this.currentPlayer;
    }


    private IStore getItemByItemID(String packageID) {
        //IStore item;
        this.parameter = new String[]{packageID};
        this.setStatement("SELECT * FROM " + this.tableName + " WHERE item_id = ? ;", this.parameter);
        return transactionBuilder(this.result);
    }

    private boolean buyItem(IStore item) {
        ICardHolderServices holder = new CardHolderServices();

        if (playerService.giveCoins(this.currentPlayer, item.getPrice())) {
            this.currentPlayer = playerService.reloadAccount(this.currentPlayer);
            if (item.getSellerID().equals(storeID)) {
                holder.sellPackage(item.getItemID(), this.currentPlayer.getUserID());

            } else {
                IPlayer seller = this.playerService.getPlayerById(item.getSellerID());
                playerService.addCoins(seller, item.getPrice());
                this.playerService.reloadAccount(seller);
                holder.changeCardHolder(this.currentPlayer.getUserID(), seller.getUserID(), item.getItemID());
            }
            deleteByID(item.getTransactionID());
            System.out.println("ITEM: " + item.getItemID() + " sold to " + currentPlayer.getUsername());

        } else {
            System.out.println(TextColor.ANSI_RED + "NOT ENOUGH MONEY" + TextColor.ANSI_RESET);
            return false;
        }
        this.currentPlayer = this.playerService.reloadAccount(this.currentPlayer);
        return true;
    }

    private void itemSetting() {

        String sellerID = currentPlayer.getUserID();
        ArrayList<IStore> transactions = getAllTransactionByUserID(sellerID);
        CardServices card = new CardServices();
        int index;
        int number = 0;
        int size = (transactions.size() - 1);
        String transactionID;
        String itemID;


        System.out.println("|#################################################|");
        System.out.println("|############   ITEM -- SETTINGS   ##############|");
        System.out.println("|#################################################|");

        for (IStore transaction : transactions) {
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

        this.currentPlayer = playerService.reloadAccount(currentPlayer);
    }

    private void changePrice(String transactionID) {
        int price = getPrice();
        this.updatePrice(transactionID, price);
        System.out.println("PRICE - UPDATE + " + transactionID);

    }

    private void removeTransaction(String transactionID, String sellerID, String itemID) {
        ICardHolderServices holder = new CardHolderServices();
        holder.updateLocked(holder.getCardHolder(sellerID, itemID), false);
        deleteByID(transactionID);
        System.out.println("TRANSACTION - REMOVED " + transactionID);
    }

    /*******************************************************************/


    /*******************************************************************/
    /**                             GETTER                            **/
    /*******************************************************************/
    private ArrayList<IStore> getAllTransaction() {
        ArrayList<IStore> transactions = new ArrayList<>();
        this.parameter = new String[]{};
        this.setStatement("SELECT * FROM " + this.tableName + ";", this.parameter);

        try {
            while (this.result.next()) {
                IStore transaction = Store.builder()
                        .transactionID(result.getString("transaction_id"))
                        .sellerID(result.getString("seller_id"))
                        .itemID(result.getString("item_id"))
                        .price(convertToDouble(result.getString("price")))
                        .timeStamp(result.getString("date"))
                        .build();
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.closeStatement();
        return transactions;
    }

    private ArrayList<IStore> getAllTransactionByUserID(String sellerID) {
        ArrayList<IStore> transactions = new ArrayList<>();
        this.parameter = new String[]{sellerID};
        this.setStatement("SELECT * FROM " + this.tableName + " WHERE seller_id = ? ;", this.parameter);

        try {
            while (this.result.next()) {
                IStore transaction = Store.builder()
                        .transactionID(result.getString("transaction_id"))
                        .sellerID(result.getString("seller_id"))
                        .itemID(result.getString("item_id"))
                        .price(convertToDouble(result.getString("price")))
                        .timeStamp(result.getString("date"))
                        .build();
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.closeStatement();
        return transactions;
    }

    private int getPrice() {
        int price = 0;
        Scanner input = new Scanner(System.in);
        System.out.println("SET NEW PRICE");
        do {
            price = input.nextInt();
            if (price < 0) {
                System.out.println("ERROR - TRY AGAIN");
            }
        } while (price < 0);
        return price;
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

    private IStore getTransactionById(String id) {
        this.parameter = new String[]{id};
        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE transaction_id = ? " + ";",
                this.parameter
        );
        return transactionBuilder(this.result);
    }

    private IStore getTransactionByUserItem(String sellerID, String itemID) {
        this.parameter = new String[]{sellerID, itemID};
        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE seller_id = ? AND item_id=? " + ";",
                this.parameter
        );
        return transactionBuilder(this.result);
    }
    /*******************************************************************/

    /*******************************************************************/
    /**                     Datenbank - Operatoren                    **/
    /*******************************************************************/
    private boolean deleteByID(String transactionID) {
        // System.out.println("#DELETE ITEM");
        this.parameter = new String[]{transactionID};
        this.setStatement("DELETE FROM " + this.tableName + " WHERE transaction_id = ? ;", this.parameter);
        this.closeStatement();
        return true;
    }

    private void updatePrice(String transactionID, int newPrice) {
        this.parameter = new String[]{"" + newPrice, transactionID};
        setStatement("UPDATE  \"" + this.tableName + "\" SET  price = ? WHERE  transaction_id= ?;", this.parameter);
    }
    /*******************************************************************/


}
