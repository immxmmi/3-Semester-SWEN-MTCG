package at.technikum.repository;

import at.technikum.model.Battle;
import at.technikum.model.Player;

import java.util.ArrayList;

public interface BattleRepository {
    Battle playGame(Battle currentBattle);

    Battle getBattleByID(String battleID);

    Battle startBattle(Player player);

}
