package at.technikum.repository;

import at.technikum.database.IDBTable;
import at.technikum.model.IPackage;
import at.technikum.model.Package;

import java.util.ArrayList;

public interface IPackageRepository extends IDBTable<IPackage> {

    Package getCurrentPackage();

    ArrayList<IPackage> loadPackages();

    ArrayList<String> loadPackageIDList();


    int getPackagePrice();

    String getRandomPackageID();

    IPackage getPackageByID(String id);

    Package insertPackage(Package newPackage);

    void deletePackageByID(String packageID);

}
