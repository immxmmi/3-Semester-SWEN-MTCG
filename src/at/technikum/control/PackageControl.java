package at.technikum.control;

import at.technikum.control.repository.Post;
import at.technikum.logger.LoggerStatic;
import at.technikum.model.repository.Package;
import at.technikum.handler.repository.PackageHandler;
import at.technikum.handler.PackageHandlerImpl;
import at.technikum.serializer.PackageSerializer;
import at.technikum.server.request.RequestImpl;
import at.technikum.server.response.ResponseBuilderImpl;
import at.technikum.server.response.ResponseImpl;
import at.technikum.model.card.cardTypes.CardElement;
import at.technikum.model.card.cardTypes.CardName;
import at.technikum.model.card.cardTypes.CardType;
import at.technikum.handler.CardHandler;
import at.technikum.utils.TextColor;
import com.google.gson.*;

public class PackageControl implements Post {


    /** --> INSTANCE **/
    private TextColor textColor;
    private CardHandler cardHandler;
    private PackageHandler packageHandler;
    private PackageSerializer packageSerializer;
    private LoggerStatic loggerStatic;


    public PackageControl() {
        this.packageHandler = new PackageHandlerImpl();
        this.packageSerializer = new PackageSerializer();
        this.cardHandler = new CardHandler();
        this.textColor = new TextColor();
        this.loggerStatic = LoggerStatic.getInstance();
    }

    /** -> CREATE PACKAGE **/
    @Override
    public ResponseImpl post(RequestImpl requestImpl) {
        loggerStatic.log("\nCREATE PACKAGE\n");

        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, true, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> WENN USER KEIN ADMIN IST **/
        if (!requestImpl.getAuth().matches("admin-mtcgToken")) {
            loggerStatic.log("\nNOT AUTH\n");
            System.out.println(this.textColor.ANSI_RED + "NOT AUTH" + this.textColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusUnAuthorized(packageSerializer.message("NOT AUTH").toString());
        }



        /** --> erstellet ein neues Package **/
        Package newPackage = new PackageHandlerImpl().getCurrentPackage();
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
            CardElement cardElement = this.cardHandler.filterCardElement(cardName);
            CardType cardType = this.cardHandler.filterCardType(cardName);
            float cardPower = cardObject.get("Damage").getAsFloat();
            newPackage.getCards().add(this.cardHandler.addCardByData(cardID, cardName, cardType, cardElement, cardPower));
        }

        /** --> neu erstelltes Package in die Datenbank hinzufügen und nach der ID fragn **/
        String packageID = this.packageHandler.insertPackage(newPackage).getPackageID();

        Package currentPackage = this.packageHandler.getItemById(packageID);

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPackage == null) {
            //System.out.println(this.textColor.ANSI_RED + "PACKAGE CREATED - ERROR" + this.textColor.ANSI_RESET);
            loggerStatic.log("\nPACKAGE CREATED - ERROR\n");
            return new ResponseBuilderImpl().statusMethodNotAllowed(packageSerializer.message("PACKAGE CREATED - ERROR").toString());
        }

        //System.out.println(this.textColor.ANSI_GREEN + "PACKAGE CREATED - OK" + this.textColor.ANSI_RESET);
        loggerStatic.log("\nPACKAGE CREATED - OK\n");

        /** --> STATUS OK **/
        JsonObject packages = this.packageSerializer.convertPackageToJson(currentPackage,true,false,false,false);
        return new ResponseBuilderImpl().statusCreated(packages.toString());
    }


}
