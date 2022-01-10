package at.technikum.repository;

import at.technikum.model.Player;
import at.technikum.model.Profil;

public interface ProfilRepository extends Repository<Profil> {

    Profil updateProfil(Profil playerInfo);

    boolean createProfil(Player currentPlayer);

    Profil getProfilByID(String userID);
}
