package at.technikum.utils.store.service;

import at.technikum.server.repository.Repository;
import at.technikum.server.request.Request;
import at.technikum.server.response.Response;
import at.technikum.utils.packages.service.IPackageService;
import at.technikum.utils.packages.service.PackageService;
import at.technikum.utils.player.IPlayer;
import at.technikum.utils.player.service.IPlayerService;
import at.technikum.utils.player.service.PlayerService;
import com.google.gson.Gson;

public class StoreServlet extends Repository {

    Gson gson;

    StoreServlet(){
        this.gson = new Gson();
    }

    @Override
    public Response GET(Request request) {
        return super.GET(request);
    }

    /**
     * --> Package kaufen
     **/
    @Override
    public Response POST(Request request) {

        /** --> Wenn REQUEST Leer ist **/
        if (request == null) {
            System.out.println(ANSI_RED + "PACKAGE BUY - ERROR" + ANSI_RESET);
            return new Response().statusBAD();
        }


        /** --> INSTANCE **/
        IPackageService packageService = new PackageService();
        IPlayerService playerService = new PlayerService();

        /** --> userID der das Package kauft **/
        String authKey = request.getAuth();
        /** --> User CHECK **/
        IPlayer currentPlayer = playerService.getPlayerById(authKey);
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "Kein User mit der ID" + ANSI_RESET);
            return new Response().statusNotFound();
        }
        /** --> Holt eine Random Package ID **/
        String packageID = packageService.getRandomPackageID();

        if (packageID.equals("0")) {
            System.out.println(ANSI_RED + "(NO PACKAGE)" + ANSI_RESET);
            return new Response().statusMethodNotAllowed();
        }
        StoreService storeService = new StoreService(currentPlayer);
        storeService.sellPackage(authKey, packageID);
        if (packageService.getPackageByID(packageID) == null) {
            System.out.println(ANSI_GREEN + "PACKAGE SOLD!" + ANSI_RESET);
        }
        return new Response().statusOK(gson.toJson("SOLD"));

    }

    @Override
    public Response PUT(Request request) {
        return super.PUT(request);
    }

    @Override
    public Response INDEX(Request request) {
        return super.INDEX(request);
    }

    @Override
    public Response DELETE(Request request) {
        return super.DELETE(request);
    }
}
