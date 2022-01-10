package at.technikum.services;

import at.technikum.model.IPackage;
import at.technikum.model.Package;
import at.technikum.repository.IStoreRepository;
import at.technikum.repository.StoreRepository;
import at.technikum.utils.card.ICard;
import at.technikum.utils.card.service.CardServices;
import at.technikum.repository.CardHolderRepository;
import at.technikum.repository.ICardHolderRepository;
import at.technikum.repository.IPackageRepository;
import at.technikum.repository.PackageRepository;
import at.technikum.utils.tools.TextColor;
import at.technikum.utils.tools.Tools;

import java.util.ArrayList;

public class PackageServices extends Tools {
    IPackageRepository packageRepository;

    public PackageServices(){
        this.packageRepository = new PackageRepository();
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
        ICardHolderRepository holder = new CardHolderRepository();
        IStoreRepository store = new StoreRepository(null);
        IPackage pack = Package.builder()
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
