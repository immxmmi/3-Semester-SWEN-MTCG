package at.technikum.model;

import at.technikum.model.repository.IDeck;
import at.technikum.model.repository.Player;
import at.technikum.model.repository.Stack;
import at.technikum.utils.Tools;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(toBuilder = true)
public class PlayerImpl extends Tools implements Player {

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
    private Stack stack;
    @Getter
    @Setter
    @SerializedName("freeStack")
    private Stack freeStack;
    @Getter
    @Setter
    @SerializedName("deck")
    private IDeck deck;


}
