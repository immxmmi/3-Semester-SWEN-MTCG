package at.technikum.model;

import at.technikum.utils.card.ICard;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Builder(toBuilder = true)
public class PackageImpl implements Package {
    @Getter
    @SerializedName("packageID")
    private String packageID;
    @Builder.Default
    @Getter
    @Setter
    @SerializedName("cards")
    private ArrayList<ICard> cards = new ArrayList<>();
    @Getter
    @Builder.Default
    @SerializedName("price")
    private int price = 5;
    @Getter
    @SerializedName("date")
    private String date;
}