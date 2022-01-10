package at.technikum.database.model;

public interface IStore{

    String getTransactionID();

    String getSellerID();

    String getItemID();

    double getPrice();

    String getTimeStamp();

}