package at.technikum.utils.packages;

import at.technikum.utils.card.ICard;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Builder(toBuilder = true)
public class Package implements IPackage {
    @Getter
    @SerializedName("packageID")
    String packageID;
    @Builder.Default
    @Getter
    @Setter
    ArrayList<ICard> cards = new ArrayList<>();
    @Getter
    @Builder.Default
    @SerializedName("price")
    int price = 5;
    @Getter
    @SerializedName("date")
    String date;
}
