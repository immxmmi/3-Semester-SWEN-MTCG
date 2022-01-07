package at.technikum.utils.stack;

import at.technikum.utils.card.ICard;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Builder(toBuilder = true)
public class Stack implements IStack {
    @Getter
    @Setter
    String userID;
    @Getter
    @Setter
    @Builder.Default
    ArrayList<ICard> stack = new ArrayList<>();
}
