package at.technikum.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
public class CardHolderImpl implements CardHolder {

    @Getter
    @SerializedName("cardHolderID")
    String cardHolderID;

    @Getter
    @SerializedName("cardID")
    String cardID;

    @Getter
    @SerializedName("holderID")
    String holderID;

    @Getter
    @SerializedName("number")
    double number;

    @Getter
    @SerializedName("locked")
    boolean locked;

}
