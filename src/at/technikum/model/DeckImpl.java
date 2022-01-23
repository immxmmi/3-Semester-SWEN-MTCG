package at.technikum.model;

import at.technikum.model.card.Card;
import at.technikum.model.repository.Deck;
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
    List<Card> deckList = new ArrayList<>();
    @Getter
    @Setter
    @Builder.Default
    @SerializedName("cardIDList")
    ArrayList<String> cardIDs = new ArrayList<>();

}
