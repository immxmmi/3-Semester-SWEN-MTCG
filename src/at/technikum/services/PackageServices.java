package at.technikum.services;

import at.technikum.model.repository.Package;
import at.technikum.model.PackageImpl;
import at.technikum.repository.StoreRepository;
import at.technikum.repository.StoreRepositoryImpl;
import at.technikum.utils.card.ICard;
import at.technikum.utils.card.service.CardServices;
import at.technikum.repository.CardHolderRepositoryImpl;
import at.technikum.repository.CardHolderRepository;
import at.technikum.repository.PackageRepository;
import at.technikum.repository.PackageRepositoryImpl;
import at.technikum.utils.TextColor;
import at.technikum.utils.Tools;

import java.util.ArrayList;

public class PackageServices extends Tools {
    PackageRepository packageRepository;

    public PackageServices(){
        this.packageRepository = new PackageRepositoryImpl();
    }

    /**
     * --> erstellt Random Packages
     **/
    public void CreateDefaultPackages(int number) {
        number += packageRepository.loadPackages().size();
        System.out.println("Packages -- PACKAGEUMBER (" + number + ")");
        int size = packageRepository.loadPackages().size();
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
        CardHolderRepository holder = new CardHolderRepositoryImpl();
        StoreRepository store = new StoreRepositoryImpl(null);
        Package pack = PackageImpl.builder()
                .packageID("PK-" + this.tokenSupplier.get())
                .build();

        ArrayList<ICard> cards = cardList.getAllCardsList();


        // Fügt Package zur Datenbank hinzu
        if (cards != null) {
            if (cards.size() > 0) {
                packageRepository.insert(pack);
                store.addPackageToStore(pack.getPackageID(), packageRepository.getPackagePrice());

                for (int i = 0; i < 5; i++) {
                    card = cards.get(    // Liste aller Karten
                            randomNumber(0, cards.size()) // random Zahl
                    );
                    //[package ID] [card ID]
                    holder.insertCardToHolder(pack.getPackageID(), card.getCardID(), true);
                }

            } else {
                System.out.println(TextColor.ANSI_RED + "NO CARDS" + TextColor.ANSI_RESET);
            }
        } else {
            System.out.println(TextColor.ANSI_RED + "NO CARDS" + TextColor.ANSI_RESET);
        }


    }

    /*******************************************************************/
}
