package at.technikum.model;

import at.technikum.utils.card.ICard;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


@Builder(toBuilder = true)
public class StoreImpl implements Store {

    @Setter
    @Getter
    @SerializedName("transactionID")
    private String transactionID;
    @Setter
    @Getter
    @SerializedName("sellerID")
    private String sellerID;

    @Setter
    @Getter
    @SerializedName("itemID")
    private String itemID;
    
    @Setter
    @Getter
    @SerializedName("price")
    private double price;

    @Setter
    @Getter
    @SerializedName("timeStamp")
    private String timeStamp;

}