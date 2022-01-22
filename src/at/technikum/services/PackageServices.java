package at.technikum.services;

import at.technikum.model.repository.Package;
import at.technikum.model.PackageImpl;
import at.technikum.handler.repository.StoreHandler;
import at.technikum.handler.StoreHandlerImpl;
import at.technikum.model.card.Card;
import at.technikum.handler.CardHandlerImpl;
import at.technikum.handler.CardHolderHandlerImpl;
import at.technikum.handler.repository.CardHolderHandler;
import at.technikum.handler.repository.PackageHandler;
import at.technikum.handler.PackageHandlerImpl;
import at.technikum.utils.TextColor;
import at.technikum.utils.Tools;

import java.util.ArrayList;

public class PackageServices extends Tools {
    PackageHandler packageHandler;

    public PackageServices(){
        this.packageHandler = new PackageHandlerImpl();
    }

    /**
     * --> erstellt Random Packages
     **/
    public void CreateDefaultPackages(int number) {
        number += packageHandler.loadPackages().size();
        System.out.println("Packages -- PACKAGEUMBER (" + number + ")");
        int size = packageHandler.loadPackages().size();
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
        CardHandlerImpl cardList = new CardHandlerImpl();
        Card card;
        CardHolderHandler holder = new CardHolderHandlerImpl();
        StoreHandler store = new StoreHandlerImpl(null);
        Package pack = PackageImpl.builder()
                .packageID("PK-" + this.tokenSupplier.get())
                .build();

        ArrayList<Card> cards = cardList.getAllCardsList();


        // Fügt Package zur Datenbank hinzu
        if (cards != null) {
            if (cards.size() > 0) {
                packageHandler.insert(pack);
                store.addPackageToStore(pack.getPackageID(), packageHandler.getPackagePrice());

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
