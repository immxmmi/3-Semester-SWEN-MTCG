package at.technikum.model.repository;

public interface Store {

    String getTransactionID();

    String getSellerID();

    String getItemID();

    double getPrice();

    String getTimeStamp();

}