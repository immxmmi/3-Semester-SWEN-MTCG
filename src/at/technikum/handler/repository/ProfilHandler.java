package at.technikum.handler.repository;

import at.technikum.model.repository.Player;
import at.technikum.model.repository.Profil;

public interface ProfilHandler extends Repository<Profil> {

    Profil updateProfil(Profil playerInfo);

    boolean createProfil(Player currentPlayer);

    Profil getItemById(String userID);
}
