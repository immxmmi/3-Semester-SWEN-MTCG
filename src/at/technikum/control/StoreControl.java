package at.technikum.control;

import at.technikum.model.IPlayer;
import at.technikum.repository.*;
import at.technikum.server.utils.request.Request;
import at.technikum.server.utils.response.Response;
import at.technikum.server.utils.response.ResponseBuilder;
import at.technikum.utils.tools.TextColor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class StoreControl {

    private Gson gson;
    private IPackageRepository packageRepository;
    private IPlayerRepository playerRepository;

    public StoreControl(){

        this.gson = new Gson();
        this.packageRepository = new PackageRepository();
        this.playerRepository = new PlayerRepository();
    }


    /**
     * --> Package kaufen
     **/
    public  Response TRANSACTION(Request request) {

        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(TextColor.ANSI_RED + "PACKAGE BUY - ERROR" + TextColor.ANSI_RESET);
            return new ResponseBuilder().statusBAD(this.Message("BAD REQUEST").toString());
        }



        /** --> userID der das Package kauft **/
        String authKey = request.getAuth();
        /** --> User CHECK **/
        IPlayer currentPlayer = this.playerRepository.getPlayerById(authKey);
        if (currentPlayer == null) {
            System.out.println(TextColor.ANSI_RED + "NO USER TOKEN" + TextColor.ANSI_RESET);
            return new ResponseBuilder().statusNotFound(this.Message("NO USER TOKEN").toString());
        }
        /** --> Holt eine Random Package ID **/
        /*String packageID = this.packageRepository.getRandomPackageID();
        if (packageID.equals("0")) {
            System.out.println(TextColor.ANSI_RED + "(NO PACKAGE)" + TextColor.ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed(this.Message("(NO PACKAGE)").toString());
        }
        */

        /** --> Holt Packages nach der Reihe **/
        String packageID = "";

        if(this.packageRepository.loadPackageIDList().size() == 0){
            System.out.println(TextColor.ANSI_RED + "(NO PACKAGE)" + TextColor.ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed(this.Message("(NO PACKAGE)").toString());
        }

        packageID = this.packageRepository.loadPackageIDList().get(0);

        StoreRepository storeRepository = new StoreRepository(currentPlayer);
        storeRepository.sellPackage(authKey, packageID);

        if (this.packageRepository.getPackageByID(packageID) != null) {
            System.out.println(TextColor.ANSI_RED + "NOT ENOUGH MONEY" + TextColor.ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed(this.Message("(NO MONEY)").toString());
        }

        return new ResponseBuilder().statusOK(this.Message("PACKAGE SOLD!").toString());

    }

    /** --> wandelt message String in JSON-Objekt um **/
    private JsonObject Message(String message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Message",message);
        return jsonObject;
    } // TODO: 10.01.2022 Fertig




}
