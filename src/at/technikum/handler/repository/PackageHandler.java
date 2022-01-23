package at.technikum.handler.repository;

import at.technikum.model.repository.Package;

import java.util.ArrayList;

public interface PackageHandler extends Repository<Package> {

    Package getCurrentPackage();

    ArrayList<Package> loadPackages();

    ArrayList<String> loadPackageIDList();


    int getPackagePrice();

    String getRandomPackageID();

    Package getItemById(String id);

    Package insertPackage(Package newPackage);

    void deletePackageByID(String packageID);

}