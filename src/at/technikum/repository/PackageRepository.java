package at.technikum.repository;

import at.technikum.database.DBTable;
import at.technikum.model.Package;

import java.util.ArrayList;

public interface PackageRepository extends DBTable<Package> {

    Package getCurrentPackage();

    ArrayList<Package> loadPackages();

    ArrayList<String> loadPackageIDList();


    int getPackagePrice();

    String getRandomPackageID();

    Package getPackageByID(String id);

    Package insertPackage(Package newPackage);

    void deletePackageByID(String packageID);

}
