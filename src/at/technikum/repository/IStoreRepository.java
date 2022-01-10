package at.technikum.repository;

import at.technikum.model.IPlayer;
import at.technikum.model.IStore;

import java.util.ArrayList;

public interface IStoreRepository {
    void addItemToStore(String itemID, int price);

    void addItemToStore(String sellerID, String itemID, double price);

    void addPackageToStore(String packageID, double price);

    IPlayer sellPackage(String customerID, String packageID);

    boolean buyItem(IStore item);

    void changePrice(String transactionID);

    void removeTransaction(String transactionID, String sellerID, String itemID);

    ArrayList<IStore> getAllTransaction();

    ArrayList<IStore> getAllTransactionByUserID(String sellerID);

    int getPrice();

    String getStoreID();
}
