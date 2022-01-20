package at.technikum.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder (toBuilder = true)
public class BattleImpl implements Battle{

    @Getter
    @Setter
    @SerializedName("battleID")
    private String battleID;
    @Getter
    @Setter
    @SerializedName("player1")
    private Player player1;
    @Getter
    @Setter
    @Builder.Default
    @SerializedName("player2")
    private Player player2 = null;
    @Getter
    @Setter
    @Builder.Default
    @SerializedName("round")
    int round = 0;
    @Getter
    @Setter
    @Builder.Default
    @SerializedName("winner")
    private Player winner = null;
    @Getter
    @Setter
    @Builder.Default
    @SerializedName("searching")
    boolean searching = true;

}
