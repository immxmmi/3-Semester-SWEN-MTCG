package at.technikum.repository;

import at.technikum.model.repository.Package;
import at.technikum.repository.util.Repository;

import java.util.ArrayList;

public interface PackageRepository extends Repository<Package> {

    Package getCurrentPackage();

    ArrayList<Package> loadPackages();

    ArrayList<String> loadPackageIDList();


    int getPackagePrice();

    String getRandomPackageID();

    Package getItemById(String id);

    Package insertPackage(Package newPackage);

    void deletePackageByID(String packageID);

}
