package at.technikum.handler.repository;

import at.technikum.model.repository.Battle;
import at.technikum.model.repository.Player;

// TODO: 23.01.2022 Fertig 
public interface BattleHandler {
    Battle playGame(Battle currentBattle);

    Battle getItemById(String battleID);

    Battle startBattle(Player player);

}
