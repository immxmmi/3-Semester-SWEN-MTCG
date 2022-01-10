package at.technikum.model;

import at.technikum.utils.card.ICard;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Builder(toBuilder = true)
public class StackImpl implements Stack {
    @Getter
    @Setter
    @SerializedName("userID")
    private String userID;
    @Getter
    @Setter
    @Builder.Default
    @SerializedName("stack")
    private ArrayList<ICard> stack = new ArrayList<>();
}
