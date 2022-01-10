package at.technikum.utils.store.service;

import at.technikum.server.utils.request.Request;
import at.technikum.server.utils.response.Response;
import at.technikum.server.utils.response.ResponseBuilder;
import at.technikum.utils.packages.service.IPackageService;
import at.technikum.utils.packages.service.PackageService;
import at.technikum.utils.player.IPlayer;
import at.technikum.utils.player.Player;
import at.technikum.utils.player.service.IPlayerService;
import at.technikum.utils.player.service.PlayerService;
import at.technikum.utils.tools.TextColor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StoreControl {

    Gson gson;

    StoreControl(){
        this.gson = new Gson();
    }


    /**
     * --> Package kaufen
     **/
    public Response TRANSACTION(Request request) {

        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(TextColor.ANSI_RED + "PACKAGE BUY - ERROR" + TextColor.ANSI_RESET);
            return new ResponseBuilder().statusBAD(this.Message("BAD REQUEST").toString());
        }


        /** --> INSTANCE **/
        IPackageService packageService = new PackageService();
        IPlayerService playerService = new PlayerService();

        /** --> userID der das Package kauft **/
        String authKey = request.getAuth();
        /** --> User CHECK **/
        IPlayer currentPlayer = playerService.getPlayerById(authKey);
        if (currentPlayer == null) {
            System.out.println(TextColor.ANSI_RED + "NO USER TOKEN" + TextColor.ANSI_RESET);
            return new ResponseBuilder().statusNotFound(this.Message("NO USER TOKEN").toString());
        }
        /** --> Holt eine Random Package ID **/
        String packageID = packageService.getRandomPackageID();

        if (packageID.equals("0")) {
            System.out.println(TextColor.ANSI_RED + "(NO PACKAGE)" + TextColor.ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed(this.Message("(NO PACKAGE)").toString());
        }
        StoreService storeService = new StoreService(currentPlayer);
        storeService.sellPackage(authKey, packageID);
        if (packageService.getPackageByID(packageID) == null) {
            System.out.println(TextColor.ANSI_GREEN + "PACKAGE SOLD!" + TextColor.ANSI_RESET);
        }

        return new ResponseBuilder().statusOK(this.Message("PACKAGE SOLD!").toString());

    }


    /** Wandelt Java Player-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    private JsonObject convertTransactionToJson(IPlayer player, boolean userID, boolean Username, boolean Password, boolean coins, boolean status, boolean stack, boolean freeStack, boolean elo){
        JsonObject object = new JsonParser().parse(gson.toJson(player, Player.class)).getAsJsonObject();
        if(!userID){object.remove("userID");}
        if(!Username){object.remove("Username");}
        if(!Password){object.remove("Password");}
        if(!coins){object.remove("coins");}
        if(!status){object.remove("status");}
        if(!stack){object.remove("stack");}
        if(!freeStack){object.remove("freeStack");}
        if(!elo){object.remove("elo");}
        object.remove("tokenSupplier");
        object.remove("token");
        return object;
    } // TODO: 10.01.2022 Fertig

    /** --> wandelt message String in JSON-Objekt um **/
    private JsonObject Message(String message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Message",message);
        return jsonObject;
    } // TODO: 10.01.2022 Fertig




}
