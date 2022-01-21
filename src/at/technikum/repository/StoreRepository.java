package at.technikum.repository;

import at.technikum.model.repository.Player;
import at.technikum.model.repository.Store;

import java.util.ArrayList;

public interface StoreRepository {
    void addItemToStore(String itemID, int price);

    void addItemToStore(String sellerID, String itemID, double price);

    void addPackageToStore(String packageID, double price);

     Player sellPackage(String customerID, String packageID);

    boolean buyItem(Store item);

    void changePrice(String transactionID);

    void removeTransaction(String transactionID, String sellerID, String itemID);

    ArrayList<Store> getAllTransaction();

    ArrayList<Store> getAllTransactionByUserID(String sellerID);

    int getPrice();

    String getStoreID();
}
