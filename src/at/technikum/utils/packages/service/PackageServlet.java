package at.technikum.utils.packages.service;

import at.technikum.utils.packages.Package;
import at.technikum.server.repository.Repository;
import at.technikum.server.request.Request;
import at.technikum.server.response.Response;
import at.technikum.utils.card.cardTypes.CardElement;
import at.technikum.utils.card.cardTypes.CardName;
import at.technikum.utils.card.cardTypes.CardType;
import at.technikum.utils.card.service.CardServices;
import com.google.gson.*;

import java.util.regex.Pattern;

public class PackageServlet extends Repository {

    PackageService packageService;
    Gson gson;
    Pattern p;

    public PackageServlet() {
        gson = new Gson();
        this.packageService = PackageService.getInstance();
    }


    @Override
    public Response GET(Request request) {

        return new Response().statusOK();
    }

    @Override
    public Response POST(Request request) {

        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(ANSI_RED + "PACKAGE CREATED - ERROR" + ANSI_RESET);
            return new Response().statusBAD();
        }
        /** --> WENN BODY LEER IST **/
        if (request.getBody().equals("")) {
            System.out.println(ANSI_RED + "PACKAGE CREATED - ERROR" + ANSI_RESET);
            return new Response().statusBAD();
        }
        /** --> WENN USER NICHT AUTH IST **/
        if (!request.getAuth().matches("Basic admin-mtcgToken")) {
            System.out.println(ANSI_RED + "NOT AUTH" + ANSI_RESET);
            return new Response().statusUnAuthorized();
        }

        /** --> INSTANCE **/
        IPackageService packageService = new PackageService();
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

        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (packageService.getPackageByID(packageID) == null) {
            System.out.println(ANSI_RED + "PACKAGE CREATED - ERROR" + ANSI_RESET);
            return new Response().statusMethodNotAllowed();
        }

        System.out.println(ANSI_GREEN + "PACKAGE CREATED - OK" + ANSI_RESET);

        /** --> STATUS OK **/
        return new Response().statusOK();
    }

    @Override
    public Response PUT(Request request) {
        return null;
    }

    @Override
    public Response INDEX(Request request) {
        return null;
    }

    @Override
    public Response DELETE(Request request) {
        return null;
    }
}
