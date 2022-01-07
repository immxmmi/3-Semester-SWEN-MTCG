package at.technikum.utils.deck;

import at.technikum.utils.card.ICard;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
public class Deck implements IDeck {
    @Getter
    @Setter
    String userID;
    @Getter
    @Setter
    @Builder.Default
    List<ICard> deckList = new ArrayList<>();
    @Getter
    @Setter
    @Builder.Default
    ArrayList<String> cardIDs = new ArrayList<>();

}
