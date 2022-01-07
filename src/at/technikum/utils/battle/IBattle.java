package at.technikum.utils.battle;

import at.technikum.utils.player.IPlayer;

import java.util.List;

public interface IBattle {

    IPlayer getPlayer1();

    IPlayer getPlayer2();

    List<IPlayer> getWinnerList();
}
