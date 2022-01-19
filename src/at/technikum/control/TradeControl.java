package at.technikum.control;

import at.technikum.model.Player;
import at.technikum.repository.*;
import at.technikum.serializer.StoreSerializer;
import at.technikum.server.utils.request.RequestImpl;
import at.technikum.server.utils.response.ResponseBuilderImpl;
import at.technikum.server.utils.response.ResponseImpl;
import at.technikum.utils.tools.TextColor;
import com.google.gson.JsonObject;

public class TradeControl {


    private PackageRepository packageRepository;
    private PlayerRepository playerRepository;
    private StoreSerializer storeSerializer;

    public TradeControl(){
        this.storeSerializer = new StoreSerializer();
        this.packageRepository = new PackageRepositoryImpl();
        this.playerRepository = new PlayerRepositoryImpl();
    }




    /** --> Trading Liste anzeigen **/
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

        if(store.getAllTransactionByUserID(currentPlayer.getUserID()).size() == 0){
            System.out.println(TextColor.ANSI_RED + "NO TRADING" + TextColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusNotFound(storeSerializer.message("NO TRADING").toString());
        };

        JsonObject jsonObject = storeSerializer.convertTransactionToJson(store.getAllTransactionByUserID(currentPlayer.getUserID()).get(0),false,false,false,true,true,false);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }

    /** --> Trading Liste anzeigen **/
    public ResponseImpl post(RequestImpl requestImpl){
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


        System.out.println(requestImpl.getBody());

        StoreRepository store = new StoreRepositoryImpl(currentPlayer);

        if(store.getAllTransactionByUserID(currentPlayer.getUserID()).size() == 0){
            System.out.println(TextColor.ANSI_RED + "NO TRADING" + TextColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusNotFound(storeSerializer.message("NO TRADING").toString());
        };

        JsonObject jsonObject = storeSerializer.convertTransactionToJson(store.getAllTransactionByUserID(currentPlayer.getUserID()).get(0),false,false,false,true,true,false);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }

}
