package at.technikum.utils.battle;

import at.technikum.model.Player;
import at.technikum.utils.tools.Tools;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
public class Battle extends Tools implements IBattle {

    @Getter
    @Setter
    Player player1;
    @Getter
    @Setter
    Player player2;
    @Getter
    @Setter
    @Builder.Default
    List<Player> WinnerList = new ArrayList<>();
    @Getter
    @Setter
    @Builder.Default
    boolean finish = false;


}
