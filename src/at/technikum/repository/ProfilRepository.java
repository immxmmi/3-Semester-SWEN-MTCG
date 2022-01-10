package at.technikum.repository;

import at.technikum.database.DBTable;
import at.technikum.model.Profil;

public interface ProfilRepository extends DBTable<Profil> {


    Profil setInfo(Profil playerInfo);

    Profil getInfoByID(String userID);
}
