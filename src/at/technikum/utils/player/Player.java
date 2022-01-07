package at.technikum.utils.player;

import at.technikum.utils.deck.IDeck;
import at.technikum.utils.stack.IStack;
import at.technikum.utils.tools.Tools;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder(toBuilder = true)
public class Player extends Tools implements IPlayer {

    /**
     * --> USER DATA
     **/
    @Setter
    @Getter
    @SerializedName("userID")
    private String userID;
    @Setter
    @Getter
    @SerializedName("Username")
    private String username;
    @Setter
    @Getter
    @SerializedName("Password")
    private String password;
    @Getter
    @Setter
    @Builder.Default
    @SerializedName("coins")
    private double coins = 20;
    @Getter
    @Setter
    @Builder.Default
    @SerializedName("elo")
    private double elo = 100;
    @Getter
    @Setter
    @Builder.Default
    @SerializedName("status")
    private boolean status = false;
    @Getter
    @Setter
    @Builder.Default
    @SerializedName("token")
    private String token = "";

    /**
     * --> DECK - STACK
     **/
    @Getter
    @Setter
    @SerializedName("stack")
    private IStack stack;
    @Getter
    @Setter
    @SerializedName("freeStack")
    private IStack freeStack;
    @Getter
    @Setter
    @SerializedName("deck")
    private IDeck deck;
    // private ArrayList<ICard> deck = new ArrayList<>();

    /**
     * --> SET DECK STACK FREESTACK
     **/
    @Override
    public void setStackDeck(IStack stack, IStack freeStack, IDeck deck) {
        this.stack = stack;
        this.freeStack = freeStack;
        this.deck = deck;
    }

    /**
     * --> AUTH - PW
     **/
    @Override
    public boolean authorizePassword(String password) {
        return this.password.equals(hashString(password));
    }

}
