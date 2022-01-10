package at.technikum.repository;

import at.technikum.database.AbstractDBTable;
import at.technikum.model.IPackage;
import at.technikum.model.Package;
import at.technikum.utils.card.ICard;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PackageRepository extends AbstractDBTable implements IPackageRepository {

    private final static int packagePrice = 5;
    private static PackageRepository instance;
    private CardHolderRepository cardHolderServices;
    @Getter
    public Package currentPackage;


    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public PackageRepository() {
        this.cardHolderServices = new CardHolderRepository();
        this.currentPackage = Package.builder()
                .packageID("PK-" + this.tokenSupplier.get())
                .price(5)
                .build();
        this.tableName = "package";
    }
    /*******************************************************************/

    /*******************************************************************/
    /**                            Builder                            **/
    /*******************************************************************/
    private Package packageBuilder(ResultSet result) {

        String packageID = "";

        try {
            if (result.next()) {
                packageID = this.result.getString("package_id");
                Package pack = Package.builder()
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

    /**
     * --> Liefert eine Liste mit alle Package (OBJEKT)
     **/
    @Override
    public ArrayList<IPackage> loadPackages() {
        ArrayList<IPackage> packages = new ArrayList<>();
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
        IPackage pack = this.loadPackages().get(0);
        return pack.getPackageID();
    }

    public static PackageRepository getInstance() {
        if (PackageRepository.instance == null) {
            PackageRepository.instance = new PackageRepository();
        }
        return PackageRepository.instance;
    }

    @Override
    public IPackage getPackageByID(String id) {
        this.parameter = new String[]{id};
        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE package_id = ? " + ";",
                this.parameter
        );
        return (IPackage) packageBuilder(this.result);
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                     Datenbank - Operatoren                    **/
    /*******************************************************************/
    @Override
    public Package insertPackage(Package newPackage) {
        //System.out.println("#INSERT:");

        ICardHolderRepository cardHolderServices = new CardHolderRepository();
        IStoreRepository storeService = new StoreRepository(null);
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
    public IPackage insert(IPackage newPackage) {
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
    public IPackage update(IPackage currentPackage) {
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
    public boolean delete(IPackage currentPackage) {
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
