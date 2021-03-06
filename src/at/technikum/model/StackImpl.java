package at.technikum.model;

import at.technikum.model.card.Card;
import at.technikum.model.repository.Stack;
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
    private ArrayList<Card> stack = new ArrayList<>();
}
