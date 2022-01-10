package at.technikum.control;

import at.technikum.model.Package;
import at.technikum.repository.PackageRepository;
import at.technikum.repository.PackageRepositoryImpl;
import at.technikum.serializer.PackageSerializer;
import at.technikum.server.utils.request.RequestImpl;
import at.technikum.server.utils.response.ResponseBuilderImpl;
import at.technikum.server.utils.response.ResponseImpl;
import at.technikum.utils.card.cardTypes.CardElement;
import at.technikum.utils.card.cardTypes.CardName;
import at.technikum.utils.card.cardTypes.CardType;
import at.technikum.utils.card.service.CardServices;
import at.technikum.utils.tools.TextColor;
import com.google.gson.*;

public class PackageControl {


    /** --> INSTANCE **/
    PackageRepository packageRepository;
    PackageSerializer packageSerializer;
    CardServices cardServices;


    public PackageControl() {
        this.packageRepository = new PackageRepositoryImpl();
        this.packageSerializer = new PackageSerializer();
        this.cardServices = new CardServices();
    }

    public ResponseImpl post(RequestImpl requestImpl) {

        /** --> Wenn REQUEST Leer ist **/
        if (requestImpl == null) {
            System.out.println(TextColor.ANSI_RED + "PACKAGE CREATED - ERROR" + TextColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusBAD(packageSerializer.message("BAD REQUEST").toString());
        }
        /** --> WENN BODY LEER IST **/
        if (requestImpl.getBody().equals("")) {
            System.out.println(TextColor.ANSI_RED + "PACKAGE CREATED - ERROR" + TextColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusBAD(packageSerializer.message("BAD REQUEST").toString());
        }

        /** --> WENN USER NICHT AUTH IST **/
        if (!requestImpl.getAuth().matches("admin-mtcgToken")) {
            System.out.println(TextColor.ANSI_RED + "NOT AUTH" + TextColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusUnAuthorized(packageSerializer.message("NOT AUTH").toString());
        }



        /** --> erstellet ein neues Package **/
        Package newPackage = new PackageRepositoryImpl().getCurrentPackage();
        /** --> Speichert den BODY in einem String **/
        String jsonString = requestImpl.getBody();
        /** --> wandelt den String in JSON-Element um **/
        JsonElement cardsElement = new JsonParser().parse(jsonString);
        /** --> wandelt das JSON-Element in ein JSON-Array **/
        JsonArray cardList = cardsElement.getAsJsonArray();

        /** --> geht jede einzelne Karte durch und fügt sie im Package und in der Datenbank ein**/
        for (JsonElement element : cardList) {
            JsonObject cardObject = element.getAsJsonObject();
            String cardID = "C-" + cardObject.get("Id").getAsString();
            CardName cardName = CardName.valueOf(cardObject.get("Name").getAsString());
            CardElement cardElement = this.cardServices.filterCardElement(cardName);
            CardType cardType = this.cardServices.filterCardType(cardName);
            float cardPower = cardObject.get("Damage").getAsFloat();
            newPackage.getCards().add(this.cardServices.addCardByData(cardID, cardName, cardType, cardElement, cardPower));
        }



        /** --> neu erstelltes Package in die Datenbank hinzufügen und nach der ID fragn **/
        String packageID = packageRepository.insertPackage(newPackage).getPackageID();

        Package currentPackage = packageRepository.getPackageByID(packageID);

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPackage == null) {
            System.out.println(TextColor.ANSI_RED + "PACKAGE CREATED - ERROR" + TextColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(packageSerializer.message("PACKAGE CREATED - ERROR").toString());
        }

        System.out.println(TextColor.ANSI_GREEN + "PACKAGE CREATED - OK" + TextColor.ANSI_RESET);

        /** --> STATUS OK **/
        JsonObject packages = packageSerializer.convertPackageToJson(currentPackage,false,false,false,true);
        return new ResponseBuilderImpl().statusOK(packages.toString());
    } // TODO: 10.01.2022 Fertig


}
