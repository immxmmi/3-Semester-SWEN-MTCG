package at.technikum.utils.card;


import at.technikum.utils.tools.Tools;
import at.technikum.utils.card.cardTypes.CardElement;
import at.technikum.utils.card.cardTypes.CardName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractCard extends Tools implements ICard {

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
