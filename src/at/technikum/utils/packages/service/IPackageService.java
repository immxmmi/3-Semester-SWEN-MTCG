package at.technikum.utils.packages.service;

import at.technikum.database.IDBTable;
import at.technikum.utils.packages.IPackage;
import at.technikum.utils.packages.Package;

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
