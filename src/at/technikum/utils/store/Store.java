package at.technikum.utils.store;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder(toBuilder = true)
public class Store implements IStore {

    @Setter
    @Getter
    private String transactionID;
    @Setter
    @Getter
    private String sellerID;

    @Setter
    @Getter
    private String itemID;

    @Setter @Getter
    private double price;

    @Setter @Getter
    String timeStamp;




}