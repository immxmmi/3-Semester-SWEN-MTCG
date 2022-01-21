package at.technikum.control;

import at.technikum.control.repository.Post;
import at.technikum.model.repository.Package;
import at.technikum.repository.PackageRepository;
import at.technikum.repository.PackageRepositoryImpl;
import at.technikum.serializer.PackageSerializer;
import at.technikum.net.server.utils.request.RequestImpl;
import at.technikum.net.server.utils.response.ResponseBuilderImpl;
import at.technikum.net.server.utils.response.ResponseImpl;
import at.technikum.utils.card.cardTypes.CardElement;
import at.technikum.utils.card.cardTypes.CardName;
import at.technikum.utils.card.cardTypes.CardType;
import at.technikum.utils.card.service.CardServices;
import at.technikum.utils.tools.TextColor;
import com.google.gson.*;

public class PackageControl implements Post {


    /** --> INSTANCE **/
    TextColor textColor;
    CardServices cardServices;
    PackageRepository packageRepository;
    PackageSerializer packageSerializer;


    public PackageControl() {
        this.packageRepository = new PackageRepositoryImpl();
        this.packageSerializer = new PackageSerializer();
        this.cardServices = new CardServices();
        this.textColor = new TextColor();
    }

    /** -> CREATE PACKAGE **/
    @Override
    public ResponseImpl post(RequestImpl requestImpl) {

        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, true, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> WENN USER KEIN ADMIN IST **/
        if (!requestImpl.getAuth().matches("admin-mtcgToken")) {
            System.out.println(this.textColor.ANSI_RED + "NOT AUTH" + this.textColor.ANSI_RESET);
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
        String packageID = this.packageRepository.insertPackage(newPackage).getPackageID();

        Package currentPackage = this.packageRepository.getPackageByID(packageID);

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPackage == null) {
            System.out.println(this.textColor.ANSI_RED + "PACKAGE CREATED - ERROR" + this.textColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(packageSerializer.message("PACKAGE CREATED - ERROR").toString());
        }

        System.out.println(this.textColor.ANSI_GREEN + "PACKAGE CREATED - OK" + this.textColor.ANSI_RESET);

        /** --> STATUS OK **/
        JsonObject packages = this.packageSerializer.convertPackageToJson(currentPackage,true,false,false,false);
        return new ResponseBuilderImpl().statusCreated(packages.toString());
    }


}
