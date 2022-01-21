package at.technikum.repository;

import at.technikum.net.database.AbstractDBTable;
import at.technikum.model.repository.Package;
import at.technikum.model.PackageImpl;
import at.technikum.utils.card.ICard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PackageRepositoryImpl extends AbstractDBTable implements PackageRepository {

    private final static int packagePrice = 5;
    private static PackageRepositoryImpl instance;
    private CardHolderRepositoryImpl cardHolderServices;
    private Package currentPackage;


    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public PackageRepositoryImpl() {
        this.cardHolderServices = new CardHolderRepositoryImpl();
        this.currentPackage = PackageImpl.builder()
                .packageID("PK-" + this.tokenSupplier.get())
                .price(5)
                .build();
        this.tableName = "package";
    }
    /*******************************************************************/

    /*******************************************************************/
    /**                            Builder                            **/
    /*******************************************************************/
    private PackageImpl packageBuilder(ResultSet result) {

        String packageID = "";

        try {
            if (result.next()) {
                packageID = this.result.getString("package_id");
                PackageImpl pack = PackageImpl.builder()
                        .packageID(packageID)
                        .cards(this.cardHolderServices.loadCardsByHolderID(packageID))
                        .date(this.result.getString("date"))
                        .build();
                return pack;
            }
        } catch (SQLException e) {

            System.out.println("GETOBJECT -ERRROR: " + e);
            e.printStackTrace();
        }
        return null;
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                             LOAD                              **/
    /*******************************************************************/
    @Override
    public Package getCurrentPackage() {
        return currentPackage;
    }



    /**
     * --> Liefert eine Liste mit alle Package (OBJEKT)
     **/
    @Override
    public ArrayList<Package> loadPackages() {
        ArrayList<Package> packages = new ArrayList<>();
        ArrayList<String> packagesID = loadPackageIDList();
        for (String i : packagesID) {
            getPackageByID(i);
            packages.add(getPackageByID(i));
        }
        return packages;
    }

    /**
     * --> Liefert eine Liste mit alle PackageIDs
     **/
    @Override
    public ArrayList<String> loadPackageIDList() {
        ArrayList<String> packagesID = new ArrayList<>();
        this.parameter = new String[]{};
        this.setStatement("SELECT * FROM " + this.tableName + " ;", this.parameter);

        try {
            while (this.result.next()) {
                packagesID.add(this.result.getString("package_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packagesID;
    }

    @Override
    public int getPackagePrice() {
        return this.packagePrice;
    }


    /*******************************************************************/
    /**                             GETTER                            **/
    /*******************************************************************/

    @Override
    public String getRandomPackageID() {

        if (this.loadPackages().size() == 0) {
            return "0";
        }
        // IPackage pack = this.loadPackages().get(randomNumber(0,this.loadPackages().size()));
        Package pack = this.loadPackages().get(0);
        return pack.getPackageID();
    }

    public static PackageRepositoryImpl getInstance() {
        if (PackageRepositoryImpl.instance == null) {
            PackageRepositoryImpl.instance = new PackageRepositoryImpl();
        }
        return PackageRepositoryImpl.instance;
    }

    @Override
    public Package getPackageByID(String id) {
        this.parameter = new String[]{id};
        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE package_id = ? " + ";",
                this.parameter
        );
        return (Package) packageBuilder(this.result);
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                     Datenbank - Operatoren                    **/
    /*******************************************************************/
    @Override
    public Package insertPackage(Package newPackage) {
        //System.out.println("#INSERT:");

        CardHolderRepository cardHolderServices = new CardHolderRepositoryImpl();
        StoreRepository storeService = new StoreRepositoryImpl(null);
        String packageID = newPackage.getPackageID();

        /** --> jede einzelene Karte im CardHolder hinzufügen **/
        for (ICard card : newPackage.getCards()) {
            cardHolderServices.insertCardToHolder(packageID, card.getCardID(), true);
        }

        /** --> package im Store hinzufügen **/
        storeService.addPackageToStore(packageID, newPackage.getPrice());


        if (newPackage == null) {
            return null;
        }
        this.parameter = new String[]{
                newPackage.getPackageID(),
                "" + formatDate(2)
        };
        this.setStatement("INSERT INTO " + this.tableName + "(\"package_id\", \"date\")VALUES(?,?);", this.parameter);
        return newPackage;
    }

    @Override
    public Package insert(Package newPackage) {
        //System.out.println("#INSERT:");
        if (newPackage == null) {
            return null;
        }
        this.parameter = new String[]{
                newPackage.getPackageID(),
                "" + formatDate(2)
        };
        this.setStatement("INSERT INTO " + this.tableName + "(\"package_id\", \"date\")VALUES(?,?);", this.parameter);
        return newPackage;
    }

    @Override
    public Package update(Package currentPackage) {
        System.out.println("#UPDATE:");
        if (currentPackage == null) {
            return null;
        }
        this.parameter = new String[]{
                currentPackage.getPackageID(),
        };

        this.setStatement(
                "UPDATE " + this.tableName +
                        "SET package_id = ? " +
                        "WHERE package_id = " + currentPackage.getPackageID() + " ;"
                , this.parameter
        );

        return getPackageByID(currentPackage.getPackageID());
    }

    @Override
    public boolean delete(Package currentPackage) {
        System.out.println("#DELETE:");
        this.parameter = new String[]{currentPackage.getPackageID()};
        this.setStatement("DELETE FROM package WHERE package_id = ? ;", this.parameter);
        this.closeStatement();
        return false;
    }

    @Override
    public void deletePackageByID(String packageID) {
        this.parameter = new String[]{packageID};
        this.setStatement("DELETE FROM " + this.tableName + " WHERE package_id = ? ;", this.parameter);
    }
    /*******************************************************************/
}
