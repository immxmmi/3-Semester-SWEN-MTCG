package at.technikum.model;

import at.technikum.model.repository.Deck;
import at.technikum.model.card.ICard;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
public class DeckImpl implements Deck {
    @Getter
    @Setter
    @SerializedName("userID")
    String userID;
    @Getter
    @Setter
    @Builder.Default
    @SerializedName("deck")
    List<ICard> deckList = new ArrayList<>();
    @Getter
    @Setter
    @Builder.Default
    @SerializedName("cardIDList")
    ArrayList<String> cardIDs = new ArrayList<>();

}
