package at.technikum.model;

import at.technikum.model.repository.Trade;
import at.technikum.model.card.ICard;
import at.technikum.model.card.cardTypes.CardType;
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
    private ICard card;

    @Setter
    @Getter
    @SerializedName("minPower")
    private double minPower;

    @Setter
    @Getter
    @SerializedName("cardTyp")
    private CardType cardTyp;

}
