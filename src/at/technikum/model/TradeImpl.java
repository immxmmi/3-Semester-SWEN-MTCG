package at.technikum.model;

import at.technikum.model.card.Card;
import at.technikum.model.card.cardTypes.CardType;
import at.technikum.model.repository.Trade;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder (toBuilder = true)
public class TradeImpl implements Trade {

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
    @SerializedName("card")
    private Card card;

    @Setter
    @Getter
    @SerializedName("minPower")
    private double minPower;

    @Setter
    @Getter
    @SerializedName("cardTyp")
    private CardType cardTyp;

}
