package at.technikum.model.repository;

import at.technikum.utils.card.ICard;

public interface Store {

    String getTransactionID();

    String getSellerID();

    String getItemID();

    double getPrice();

    String getTimeStamp();

}