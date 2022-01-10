package at.technikum.utils.battle;

import at.technikum.model.Player;

import java.util.List;

public interface IBattle {

    Player getPlayer1();

    Player getPlayer2();

    List<Player> getWinnerList();
}
