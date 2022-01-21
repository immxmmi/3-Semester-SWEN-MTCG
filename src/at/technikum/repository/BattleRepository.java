package at.technikum.repository;

import at.technikum.model.repository.Battle;
import at.technikum.model.repository.Player;

public interface BattleRepository {
    Battle playGame(Battle currentBattle);

    Battle getItemById(String battleID);

    Battle startBattle(Player player);

}
