package at.technikum.control;

import at.technikum.control.repository.Post;
import at.technikum.handler.PackageHandlerImpl;
import at.technikum.handler.PlayerHandlerImpl;
import at.technikum.handler.StoreHandlerImpl;
import at.technikum.handler.repository.PackageHandler;
import at.technikum.handler.repository.PlayerHandler;
import at.technikum.handler.repository.StoreHandler;
import at.technikum.logger.LoggerStatic;
import at.technikum.model.repository.Player;
import at.technikum.serializer.StoreSerializer;
import at.technikum.server.request.RequestImpl;
import at.technikum.server.response.ResponseBuilderImpl;
import at.technikum.server.response.ResponseImpl;

public class StoreControl implements Post {
    private PackageHandler packageHandler;
    private PlayerHandler playerHandler;
    private StoreSerializer storeSerializer;
    private LoggerStatic loggerStatic;

    public StoreControl(){
        this.storeSerializer = new StoreSerializer();
        this.packageHandler = new PackageHandlerImpl();
        this.playerHandler = new PlayerHandlerImpl();
        this.loggerStatic = LoggerStatic.getInstance();
    }


    /*** --> Package kaufen **/
    public ResponseImpl post(RequestImpl requestImpl) {
        loggerStatic.log("\n# PACKAGE - BUY\n");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }


        /** --> userID der das Package kauft **/
        String authKey = requestImpl.getAuth();
        /** --> User CHECK **/
        Player currentPlayer = this.playerHandler.getItemById(authKey);

        /** --> Holt eine Random Package ID **/
        /*String packageID = this.packageRepository.getRandomPackageID();
        if (packageID.equals("0")) {
            System.out.println(TextColor.ANSI_RED + "(NO PACKAGE)" + TextColor.ANSI_RESET);
            return new ResponseBuilder().statusMethodNotAllowed(this.Message("(NO PACKAGE)").toString());
        }
        */

        /** --> Holt Packages nach der Reihe **/
        String packageID = "";

        if(this.packageHandler.loadPackageIDList().size() == 0){
            //System.out.println(TextColor.ANSI_RED + "(NO PACKAGE)" + TextColor.ANSI_RESET);
            loggerStatic.log("\n(NO PACKAGE)\n");
            return new ResponseBuilderImpl().statusMethodNotAllowed(storeSerializer.message("(NO PACKAGE)").toString());
        }

        packageID = this.packageHandler.loadPackageIDList().get(0);

        StoreHandler store = new StoreHandlerImpl(currentPlayer);
        store.sellPackage(authKey, packageID);

        if (this.packageHandler.getItemById(packageID) != null) {
            //System.out.println(TextColor.ANSI_RED + "NOT ENOUGH MONEY" + TextColor.ANSI_RESET);
            loggerStatic.log("\n(NO MONEY)\n");
            return new ResponseBuilderImpl().statusMethodNotAllowed(storeSerializer.message("(NO MONEY)").toString());
        }
        loggerStatic.log("\nPACKAGE SOLD!\n");
        return new ResponseBuilderImpl().statusOK(storeSerializer.message("PACKAGE SOLD!").toString());

    }


}
