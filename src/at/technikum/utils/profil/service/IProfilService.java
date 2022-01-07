package at.technikum.utils.profil.service;

import at.technikum.database.IDBTable;
import at.technikum.utils.profil.IProfil;

public interface IProfilService extends IDBTable<IProfil> {


    IProfil setInfo(IProfil playerInfo);

    void printPlayerInfo(IProfil playerInfo);

    IProfil getInfoByID(String userID);
}
