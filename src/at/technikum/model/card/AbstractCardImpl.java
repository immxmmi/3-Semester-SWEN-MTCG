package at.technikum.model.card;

import at.technikum.model.card.cardTypes.CardElement;
import at.technikum.model.card.cardTypes.CardName;
import com.google.gson.annotations.SerializedName;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor

public abstract class AbstractCardImpl implements Card {
    @Setter
    @Getter
    @SerializedName("Id")
    String cardID;

    @Getter
    @SerializedName("Name")
    CardName cardName;

    @Getter
    @SerializedName("Damage")
    double cardPower;

    @Getter
    @SerializedName("cardElement")
    CardElement cardElement;

}
