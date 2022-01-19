package at.technikum.repository;

import at.technikum.database.AbstractDBTable;
import at.technikum.model.Player;
import at.technikum.model.Store;
import at.technikum.model.StoreImpl;
import at.technikum.utils.Printer;
import at.technikum.utils.PrinterImpl;
import at.technikum.utils.card.ICard;
import at.technikum.utils.card.service.CardServices;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class StoreRepositoryImpl extends AbstractDBTable implements StoreRepository {
    private static final String storeID = "STORE - ST1120";
    private Printer printer;
    private Player currentPlayer;
    private PlayerRepositoryImpl playerRepositoryImpl = new PlayerRepositoryImpl();
    private CardServices cardServices = new CardServices();


    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public StoreRepositoryImpl(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.tableName = "store";
        this.printer = new PrinterImpl();
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                            BUILDER                            **/
    /*******************************************************************/
    private Store transactionBuilder(ResultSet result) {

        try {
            if (result.next()) {
                Store item = StoreImpl.builder()
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
    @Override
    public void addItemToStore(String itemID, int price) {

        CardHolderRepository holder = new CardHolderRepositoryImpl();

        holder.updateLocked(holder.getCardHolder(this.currentPlayer.getUserID(), itemID), true);

        //ICardHolder holder1 = holder.get
        //holder.updateLocked();

        addItemToStore(this.currentPlayer.getUserID(), itemID, price);
    }
    @Override
    public void addItemToStore(String sellerID, String itemID, double price) {
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
    /**                            ACTION                             **/
    /*******************************************************************/
    @Override
    public Player sellPackage(String customerID, String packageID) {
        PlayerRepositoryImpl playerRepositoryImpl = new PlayerRepositoryImpl();
        this.currentPlayer = playerRepositoryImpl.getPlayerById(customerID);
        buyItem(getItemByItemID(packageID));
        return this.currentPlayer;
    }

    @Override
    public boolean buyItem(Store item) {
        CardHolderRepository holder = new CardHolderRepositoryImpl();

        if (playerRepositoryImpl.giveCoins(this.currentPlayer, item.getPrice())) {
            this.currentPlayer = playerRepositoryImpl.reloadAccount(this.currentPlayer);
            if (item.getSellerID().equals(storeID)) {
                holder.sellPackage(item.getItemID(), this.currentPlayer.getUserID());

            } else {
                Player seller = this.playerRepositoryImpl.getPlayerById(item.getSellerID());
                playerRepositoryImpl.addCoins(seller, item.getPrice());
                this.playerRepositoryImpl.reloadAccount(seller);
                holder.changeCardHolder(this.currentPlayer.getUserID(), seller.getUserID(), item.getItemID());
            }
            deleteByID(item.getTransactionID());
            System.out.println("ITEM: " + item.getItemID() + " sold to " + currentPlayer.getUsername());

        } else {
            //System.out.println(TextColor.ANSI_RED + "NOT ENOUGH MONEY" + TextColor.ANSI_RESET);
            return false;
        }
        this.currentPlayer = this.playerRepositoryImpl.reloadAccount(this.currentPlayer);
        return true;
    }

    @Override
    public void changePrice(String transactionID) {
        int price = getPrice();
        this.updatePrice(transactionID, price);
        System.out.println("PRICE - UPDATE + " + transactionID);

    }

    @Override
    public void removeTransaction(String transactionID, String sellerID, String itemID) {
        CardHolderRepository holder = new CardHolderRepositoryImpl();
        holder.updateLocked(holder.getCardHolder(sellerID, itemID), false);
        deleteByID(transactionID);
        System.out.println("TRANSACTION - REMOVED " + transactionID);
    }

    /*******************************************************************/


    /*******************************************************************/
    /**                             GETTER                            **/
    /*******************************************************************/
    @Override
    public ArrayList<Store> getAllTransaction() {
        ArrayList<Store> transactions = new ArrayList<>();
        this.parameter = new String[]{};
        this.setStatement("SELECT * FROM " + this.tableName + ";", this.parameter);

        try {
            while (this.result.next()) {
                Store transaction = StoreImpl.builder()
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
    @Override
    public ArrayList<Store> getAllTransactionByUserID(String sellerID) {
        ArrayList<Store> transactions = new ArrayList<>();
        this.parameter = new String[]{sellerID};
        this.setStatement("SELECT * FROM " + this.tableName + " WHERE seller_id = ? ;", this.parameter);

        try {
            while (this.result.next()) {
                Store transaction = StoreImpl.builder()
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




    private Store getItemByItemID(String packageID) {
        //IStore item;
        this.parameter = new String[]{packageID};
        this.setStatement("SELECT * FROM " + this.tableName + " WHERE item_id = ? ;", this.parameter);
        return transactionBuilder(this.result);
    }
    private Store getTransactionById(String id) {
        this.parameter = new String[]{id};
        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE transaction_id = ? " + ";",
                this.parameter
        );
        return transactionBuilder(this.result);
    }
    private Store getTransactionByUserItem(String sellerID, String itemID) {
        this.parameter = new String[]{sellerID, itemID};
        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE seller_id = ? AND item_id=? " + ";",
                this.parameter
        );
        return transactionBuilder(this.result);
    }
    @Override
    public int getPrice() {
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
    @Override
    public String getStoreID() {
        return storeID;
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
