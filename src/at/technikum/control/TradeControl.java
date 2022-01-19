package at.technikum.control;

import at.technikum.model.Package;
import at.technikum.model.Player;
import at.technikum.model.PlayerImpl;
import at.technikum.model.Trade;
import at.technikum.model.TradeImpl;
import at.technikum.repository.*;
import at.technikum.serializer.StoreSerializer;
import at.technikum.serializer.TradeSerializer;
import at.technikum.server.utils.request.RequestImpl;
import at.technikum.server.utils.response.ResponseBuilderImpl;
import at.technikum.server.utils.response.ResponseImpl;
import at.technikum.utils.card.cardTypes.CardElement;
import at.technikum.utils.card.cardTypes.CardName;
import at.technikum.utils.card.cardTypes.CardType;
import at.technikum.utils.tools.TextColor;
import com.google.gson.*;

public class TradeControl {


    private PackageRepository packageRepository;
    private PlayerRepository playerRepository;
    private TradeSerializer tradeSerializer;
    private TextColor textColor;
    private TradeRepository trade;
    Gson gson;

    public TradeControl(){
        this.tradeSerializer = new TradeSerializer();
        this.packageRepository = new PackageRepositoryImpl();
        this.playerRepository = new PlayerRepositoryImpl();
        this.textColor = new TextColor();
        this.trade = new TradeRepositoryImpl();
        this.gson = new Gson();
    }




    /** --> Trading Liste anzeigen **/
    public ResponseImpl get(RequestImpl requestImpl){

        /** --> Wenn REQUEST Leer ist **/
        if (requestImpl == null) {
            System.out.println(textColor.ANSI_RED + "TRADE - ERROR" + textColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusBAD(tradeSerializer.message("BAD REQUEST").toString());
        }

        /** --> userID -> Trading Liste **/
        String userID = requestImpl.getAuth();
        /** --> User CHECK **/
        Player currentPlayer = this.playerRepository.getPlayerById(userID);
        if (currentPlayer == null) {
            System.out.println(TextColor.ANSI_RED + "NO USER TOKEN" + TextColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusNotFound(tradeSerializer.message("NO USER TOKEN").toString());
        }

        if(trade.getAllTradeByUserID(currentPlayer.getUserID()).size() == 0){
            System.out.println(TextColor.ANSI_RED + "NO TRADING" + TextColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusNotFound(tradeSerializer.message("NO TRADING").toString());
        };

        JsonObject jsonObject = tradeSerializer.convertTradeToJson(trade.getAllTradeByUserID(currentPlayer.getUserID()).get(0),true,true,true,true,true);
        /** --> STATUS OK **/
        System.out.println(textColor.ANSI_GREEN + "LOADING FINISHED!" + textColor.ANSI_RESET);
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }

    /** --> Trading Liste anzeigen **/
    public ResponseImpl post(RequestImpl requestImpl){

        /** --> WENN REQUEST LEER IST --> WENN BODY LEER IST **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> userID -> Trading Liste **/
        String userID = requestImpl.getAuth();

        /** --> User CHECK **/
        Player currentPlayer = this.playerRepository.getPlayerById(userID);
        if (currentPlayer == null) {
            System.out.println(textColor.ANSI_RED + "NO USER TOKEN" + textColor.ANSI_RESET);
            return new ResponseBuilderImpl().statusNotFound(tradeSerializer.message("NO USER TOKEN").toString());
        }
        /** --> Speichert den BODY in einem String **/
        String jsonString = requestImpl.getBody();
        /** --> wandelt den String in JSON-Element um **/
        JsonElement tradeElement = new JsonParser().parse(jsonString);
        JsonObject tradeObject = tradeElement.getAsJsonObject();
        String tradeID = tradeObject.get("Id").toString().replace("\"","");
        String cardID = "C-" + tradeObject.get("CardToTrade").toString().replace("\"","");
        CardType cardTyp = CardType.valueOf(tradeObject.get("Type").toString().replace("\"","").toUpperCase());
        double cardPower = tradeObject.get("MinimumDamage").getAsDouble();
        if(trade.addTrade(tradeID,currentPlayer.getUserID(),cardID,cardTyp,cardPower) == null){
                System.out.println(textColor.ANSI_RED + "TRADE FAILED" + textColor.ANSI_RESET);
                return new ResponseBuilderImpl().statusNotFound(tradeSerializer.message("TRADE FAILED").toString());
        };

        /** --> STATUS OK **/
        System.out.println(textColor.ANSI_GREEN + "CREATE TRADE SUCCESS" + textColor.ANSI_RESET);
        return new ResponseBuilderImpl().statusOK(tradeSerializer.message("CREATE TRADE SUCCESS").toString());
    }

}
