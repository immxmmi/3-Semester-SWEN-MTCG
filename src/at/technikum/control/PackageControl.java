package at.technikum.control;

import at.technikum.server.utils.request.Request;
import at.technikum.server.utils.response.Response;
import at.technikum.server.utils.response.ResponseBuilder;
import at.technikum.utils.card.cardTypes.CardElement;
import at.technikum.utils.card.cardTypes.CardName;
import at.technikum.utils.card.cardTypes.CardType;
import at.technikum.utils.card.service.CardServices;
import at.technikum.model.IPackage;
import at.technikum.model.Package;
import at.technikum.repository.IPackageRepository;
import at.technikum.repository.PackageRepository;
import at.technikum.utils.tools.TextColor;
import com.google.gson.*;

public class PackageControl {

    PackageRepository packageRepository;
    Gson gson;

    public PackageControl() {
        gson = new Gson();
        this.packageRepository = PackageRepository.getInstance();
    }

    public Response PACKAGE(Request request) {

        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(TextColor.ANSI_RED + "PACKAGE CREATED - ERROR" + TextColor.ANSI_RESET);
            return new ResponseBuilder().statusBAD(this.Message("BAD REQUEST").toString());
        }
        /** --> WENN BODY LEER IST **/
        if (request.getBody().equals("")) {
            System.out.println(TextColor.ANSI_RED + "PACKAGE CREATED - ERROR" + TextColor.ANSI_RESET);
            return new ResponseBuilder().statusBAD(this.Message("BAD REQUEST").toString());
        }

        System.out.println(request.getAuth());
        /** --> WENN USER NICHT AUTH IST **/
        if (!request.getAuth().matches("admin-mtcgToken")) {
            System.out.println(TextColor.ANSI_RED + "NOT AUTH" + TextColor.ANSI_RESET);
            return new ResponseBuilder().statusUnAuthorized(this.Message("NOT AUTH").toString());
        }

        /** --> INSTANCE **/
        IPackageRepository packageService = new PackageRepository();
        CardServices cardServices = new CardServices();

        /** --> erstellet ein neues Package **/
        Package newPackage = packageService.getCurrentPackage();
        /** --> Speichert den BODY in einem String **/
        String jsonString = request.getBody();
        /** --> wandelt den String in JSON-Element um **/
        JsonElement cardsElement = new JsonParser().parse(jsonString);
        /** --> wandelt das JSON-Element in ein JSON-Array **/
        JsonArray cardList = cardsElement.getAsJsonArray();

        /** --> geht jede einzelne Karte durch und fügt sie im Package und in der Datenbank ein**/
        for (JsonElement element : cardList) {
            JsonObject cardObject = element.getAsJsonObject();
            String cardID = "C-" + cardObject.get("Id").getAsString();
            CardName cardName = CardName.valueOf(cardObject.get("Name").getAsString());
            CardElement cardElement = cardServices.filterCardElement(cardName);
            CardType cardType = cardServices.filterCardType(cardName);
            float cardPower = cardObject.get("Damage").getAsFloat();
            newPackage.getCards().add(cardServices.addCardByData(cardID, cardName, cardType, cardElement, cardPower));
        }

        /** --> neu erstelltes Package in die Datenbank hinzufügen und nach der ID fragn **/
        String packageID = packageService.insertPackage(newPackage).getPackageID();

        IPackage currentPackage = packageService.getPackageByID(packageID);

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPackage == null) {
            System.out.println(TextColor.ANSI_RED + "PACKAGE CREATED - ERROR" + TextColor.ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed(this.Message("PACKAGE CREATED - ERROR").toString());
        }

        System.out.println(TextColor.ANSI_GREEN + "PACKAGE CREATED - OK" + TextColor.ANSI_RESET);

        /** --> STATUS OK **/
        JsonObject packages = convertPackageToJson(currentPackage,true,true,true,true);
        return new ResponseBuilder().statusOK(packages.toString());
    } // TODO: 10.01.2022 Fertig

    /** Wandelt Java Package-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    private JsonObject convertPackageToJson(IPackage packages,boolean packageID, boolean cards, boolean price, boolean date){
        JsonObject object = new JsonParser().parse(gson.toJson(packages, Package.class)).getAsJsonObject();
        if(!packageID){object.remove("packageID");}
        if(!cards){object.remove("cards");}
        if(!price){object.remove("price");}
        if(!date){object.remove("date");}
        return object;
    } // TODO: 10.01.2022 Fertig

    /** --> wandelt message String in JSON-Objekt um **/
    private JsonObject Message(String message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Message",message);
        return jsonObject;
    } // TODO: 10.01.2022 Fertig
}
