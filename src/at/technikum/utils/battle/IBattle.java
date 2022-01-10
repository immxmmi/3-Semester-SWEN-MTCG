package at.technikum.utils.battle;

import at.technikum.model.IPlayer;

import java.util.List;

public interface IBattle {

    IPlayer getPlayer1();

    IPlayer getPlayer2();

    List<IPlayer> getWinnerList();
}
