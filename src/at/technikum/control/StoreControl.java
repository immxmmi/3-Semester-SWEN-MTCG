package at.technikum.control;

import at.technikum.control.repository.Post;
import at.technikum.model.repository.Player;
import at.technikum.repository.*;
import at.technikum.serializer.StoreSerializer;
import at.technikum.net.server.utils.request.RequestImpl;
import at.technikum.net.server.utils.response.ResponseBuilderImpl;
import at.technikum.net.server.utils.response.ResponseImpl;
import at.technikum.utils.tools.TextColor;

public class StoreControl implements Post {

    private PackageRepository packageRepository;
    private PlayerRepository playerRepository;
    private StoreSerializer storeSerializer;

    public StoreControl(){
        this.storeSerializer = new StoreSerializer();
        this.packageRepository = new PackageRepositoryImpl();
        this.playerRepository = new PlayerRepositoryImpl();
    }


    /*** --> Package kaufen **/
    public ResponseImpl post(RequestImpl requestImpl) {

        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }


        /** --> userID der das Package kauft **/
        String authKey = requestImpl.getAuth();
        /** --> User CHECK **/
        Player currentPlayer = this.playerRepository.getPlayerById(authKey);

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




}
