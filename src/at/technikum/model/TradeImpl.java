package at.technikum.model;

import at.technikum.utils.card.cardTypes.CardType;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder (toBuilder = true)
public class TradeImpl {

    @Setter
    @Getter
    @SerializedName("tradeID")
    private String tradeID;
    @Setter
    @Getter
    @SerializedName("userID")
    private String userID;

    @Setter
    @Getter
    @SerializedName("cardID")
    private String cardID;

    @Setter
    @Getter
    @SerializedName("minPower")
    private double minPower;

    @Setter
    @Getter
    @SerializedName("cardTyp")
    private CardType cardTyp;

}
