package at.technikum.utils.packages.service;

import at.technikum.database.IDBTable;
import at.technikum.database.model.IPackage;
import at.technikum.database.model.Package;

import java.util.ArrayList;

public interface IPackageService extends IDBTable<IPackage> {

    Package getCurrentPackage();

    ArrayList<IPackage> loadPackages();

    ArrayList<String> loadPackageIDList();

    void CreateDefaultPackages(int number);

    String getRandomPackageID();

    IPackage getPackageByID(String id);

    Package insertPackage(Package newPackage);

    void deletePackageByID(String packageID);

}
