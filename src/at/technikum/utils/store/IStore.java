package at.technikum.utils.store;

public interface IStore{

    String getTransactionID();

    String getSellerID();

    String getItemID();

    double getPrice();

    String getTimeStamp();

}