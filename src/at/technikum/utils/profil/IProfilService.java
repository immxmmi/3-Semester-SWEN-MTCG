package at.technikum.utils.profil;

import at.technikum.database.IDBTable;
import at.technikum.database.model.IProfil;

public interface IProfilService extends IDBTable<IProfil> {


    IProfil setInfo(IProfil playerInfo);

    void printPlayerInfo(IProfil playerInfo);

    IProfil getInfoByID(String userID);
}
