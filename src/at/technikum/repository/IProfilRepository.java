package at.technikum.repository;

import at.technikum.database.IDBTable;
import at.technikum.model.IProfil;

public interface IProfilRepository extends IDBTable<IProfil> {


    IProfil setInfo(IProfil playerInfo);

    IProfil getInfoByID(String userID);
}
