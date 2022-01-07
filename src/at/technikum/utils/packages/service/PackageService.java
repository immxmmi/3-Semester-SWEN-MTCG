package at.technikum.utils.packages.service;


import at.technikum.database.AbstractDBTable;
import at.technikum.utils.card.ICard;
import at.technikum.utils.card.service.CardServices;
import at.technikum.utils.cardHolder.service.CardHolderServices;
import at.technikum.utils.cardHolder.service.ICardHolderServices;
import at.technikum.utils.packages.IPackage;
import at.technikum.utils.packages.Package;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PackageService extends AbstractDBTable implements IPackageService {

    static final int packagePrice = 5;
    private static PackageService instance;
    @Getter
    public Package currentPackage;

    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public PackageService() {
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

        try {
            if (result.next()) {
                Package pack = Package.builder()
                        .packageID(this.result.getString("package_id"))
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

    /**
     * --> erstellt Random Packages
     **/
    @Override
    public void CreateDefaultPackages(int number) {
        number += this.loadPackages().size();
        System.out.println("Packages -- PACKAGEUMBER (" + number + ")");
        int size = this.loadPackages().size();
        for (int i = size; i <= number; i++) {
            this.PackageFactory();
            i++;
        }
    }

    /*******************************************************************/


    /*******************************************************************/
    /**                             FACTORY                           **/
    /*******************************************************************/
    private void PackageFactory() {
        System.out.println("#CREATE NEW PACKAGE");
        CardServices cardList = new CardServices();
        ICard card;
        ICardHolderServices holder = new CardHolderServices();
        //IStoreService store = new StoreService(null);
        IPackage pack = Package.builder()
                .packageID("PK-" + this.tokenSupplier.get())
                .build();

        ArrayList<ICard> cards = cardList.getAllCardsList();


        // Fügt Package zur Datenbank hinzu
        if (cards != null) {
            if (cards.size() > 0) {
                this.insert(pack);
               // store.addPackageToStore(pack.getPackageID(), packagePrice);

                for (int i = 0; i < 5; i++) {
                    card = cards.get(    // Liste aller Karten
                            randomNumber(0, cards.size()) // random Zahl
                    );
                    //[package ID] [card ID]
                    holder.insertCardToHolder(pack.getPackageID(), card.getCardID(), true);
                }

            } else {
                System.out.println(ANSI_RED + "NO CARDS" + ANSI_RESET);
            }
        } else {
            System.out.println(ANSI_RED + "NO CARDS" + ANSI_RESET);
        }


    }

    /*******************************************************************/


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

    public static PackageService getInstance() {
        if (PackageService.instance == null) {
            PackageService.instance = new PackageService();
        }
        return PackageService.instance;
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
     return null;
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
