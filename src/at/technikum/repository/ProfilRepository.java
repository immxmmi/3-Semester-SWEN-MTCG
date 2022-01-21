package at.technikum.repository;

import at.technikum.model.repository.Player;
import at.technikum.model.repository.Profil;
import at.technikum.repository.util.Repository;

public interface ProfilRepository extends Repository<Profil> {

    Profil updateProfil(Profil playerInfo);

    boolean createProfil(Player currentPlayer);

    Profil getProfilByID(String userID);
}
