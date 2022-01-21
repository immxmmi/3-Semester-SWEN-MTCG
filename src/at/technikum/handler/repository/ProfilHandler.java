package at.technikum.handler.repository;

import at.technikum.model.repository.Player;
import at.technikum.model.repository.Profil;
import at.technikum.handler.repository.Repository;

public interface ProfilHandler extends Repository<Profil> {

    Profil updateProfil(Profil playerInfo);

    boolean createProfil(Player currentPlayer);

    Profil getItemById(String userID);
}
