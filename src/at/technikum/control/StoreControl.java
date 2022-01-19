package at.technikum.control;

import at.technikum.model.Player;
import at.technikum.repository.*;
import at.technikum.serializer.StoreSerializer;
import at.technikum.server.utils.request.RequestImpl;
import at.technikum.server.utils.response.ResponseBuilderImpl;
import at.technikum.server.utils.response.ResponseImpl;
import at.technikum.utils.tools.TextColor;

public class StoreControl{

    private PackageRepository packageRepository;
    private PlayerRepository playerRepository;
    private StoreSerializer storeSerializer;

    public StoreControl(){
        this.storeSerializer = new StoreSerializer();
        this.packageRepository = new PackageRepositoryImpl();
        this.playerRepository = new PlayerRepositoryImpl();
    }


    /**
     * --> Package kaufen
     **/
    public ResponseImpl post(RequestImpl requestImpl) {

        /** --> Wenn REQUEST Leer ist **/
        if (requestImpl == null) {
            System.out.println(TextColor.ANSI_RED + "PACKAGE BUY - ERROR" + TextColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusBAD(storeSerializer.message("BAD REQUEST").toString());
        }



        /** --> userID der das Package kauft **/
        String authKey = requestImpl.getAuth();
        /** --> User CHECK **/
        Player currentPlayer = this.playerRepository.getPlayerById(authKey);
        if (currentPlayer == null) {
            System.out.println(TextColor.ANSI_RED + "NO USER TOKEN" + TextColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusNotFound(storeSerializer.message("NO USER TOKEN").toString());
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
            return new ResponseBuilderImpl().statusMethodNotAllowed(storeSerializer.message("(NO PACKAGE)").toString());
        }

        packageID = this.packageRepository.loadPackageIDList().get(0);

        StoreRepository store = new StoreRepositoryImpl(currentPlayer);
        store.sellPackage(authKey, packageID);

        if (this.packageRepository.getPackageByID(packageID) != null) {
            System.out.println(TextColor.ANSI_RED + "NOT ENOUGH MONEY" + TextColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(storeSerializer.message("(NO MONEY)").toString());
        }

        return new ResponseBuilderImpl().statusOK(storeSerializer.message("PACKAGE SOLD!").toString());

    }

    public ResponseImpl get(RequestImpl requestImpl){
        /** --> Wenn REQUEST Leer ist **/
        if (requestImpl == null) {
            System.out.println(TextColor.ANSI_RED + "TRADE - ERROR" + TextColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusBAD(storeSerializer.message("BAD REQUEST").toString());
        }
        /** --> userID -> Trading Liste **/
        String userID = requestImpl.getAuth();

        /** --> User CHECK **/
        Player currentPlayer = this.playerRepository.getPlayerById(userID);
        if (currentPlayer == null) {
            System.out.println(TextColor.ANSI_RED + "NO USER TOKEN" + TextColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusNotFound(storeSerializer.message("NO USER TOKEN").toString());
        }

        StoreRepository store = new StoreRepositoryImpl(currentPlayer);

        store.getAllTransactionByUserID(currentPlayer.getUserID());

        storeSerializer.convertTransactionToJson(store.getAllTransactionByUserID(currentPlayer.getUserID()).get(0),true,false,false,false,false);

        return new ResponseBuilderImpl().statusOK(storeSerializer.message("GET!").toString());
    }





}
