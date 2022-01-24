package at.technikum.control;

import at.technikum.handler.repository.CardHolderHandler;
import at.technikum.handler.repository.PlayerHandler;
import at.technikum.handler.repository.TradeHandler;
import at.technikum.logger.LoggerStatic;
import at.technikum.model.repository.Player;
import at.technikum.model.repository.Trade;
import at.technikum.handler.*;
import at.technikum.serializer.TradeSerializer;
import at.technikum.server.request.RequestImpl;
import at.technikum.server.response.ResponseBuilderImpl;
import at.technikum.server.response.ResponseImpl;
import at.technikum.model.card.cardTypes.CardType;
import at.technikum.utils.TextColor;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TradeControl {

    private CardHolderHandler cardHolderHandler;
    private PlayerHandler playerHandler;
    private TradeSerializer tradeSerializer;
    private TextColor textColor;
    private TradeHandler trade;
    private LoggerStatic loggerStatic;
    private Gson gson;

    public TradeControl(){
        this.tradeSerializer = new TradeSerializer();
        this.cardHolderHandler = new CardHolderHandlerImpl();
        this.playerHandler = new PlayerHandlerImpl();
        this.textColor = new TextColor();
        this.trade = new TradeHandlerImpl();
        this.gson = new Gson();
        this.loggerStatic = LoggerStatic.getInstance();
    }

    /** --> Trading Liste anzeigen **/
    public ResponseImpl get(RequestImpl requestImpl){
        loggerStatic.log("\nTRADE\n");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> userID -> Trading Liste **/
        String userID = requestImpl.getAuth();
        /** --> User CHECK **/
        Player currentPlayer = this.playerHandler.getItemById(userID);


        if(trade.getAllTradeByUserID(currentPlayer.getUserID()).size() == 0){
            //System.out.println(TextColor.ANSI_RED + "NO TRADING" + TextColor.ANSI_RESET);
            loggerStatic.log("\nNO TRADING\n");
            return new ResponseBuilderImpl().statusNotFound(tradeSerializer.message("NO TRADING").toString());
        };

        JsonObject jsonObject = tradeSerializer.convertTradeToJson(trade.getAllTradeByUserID(currentPlayer.getUserID()).get(0),true,true,true,true,true);
        /** --> STATUS OK **/
        //System.out.println(textColor.ANSI_GREEN + "LOADING FINISHED!" + textColor.ANSI_RESET);
        loggerStatic.log("\nLOADING FINISHED!\n");
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }

    /** --> Trade erstellen **/
    public ResponseImpl post(RequestImpl requestImpl){

        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, true, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> userID -> Trading Liste **/
        String userID = requestImpl.getAuth();

        /** --> User CHECK **/
        Player currentPlayer = this.playerHandler.getItemById(userID);

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
            //System.out.println(textColor.ANSI_RED + "TRADE FAILED" + textColor.ANSI_RESET);
            loggerStatic.log("\nTRADE FAILED\n");
            return new ResponseBuilderImpl().statusNotFound(tradeSerializer.message("TRADE FAILED").toString());
        };

        /** --> STATUS OK **/
        //System.out.println(textColor.ANSI_GREEN + "CREATE TRADE SUCCESS" + textColor.ANSI_RESET);
        loggerStatic.log("\nCREATE TRADE SUCCESS\n");
        return new ResponseBuilderImpl().statusCreated(tradeSerializer.message("CREATE TRADE SUCCESS").toString());
    }

    /** --> Trade **/
    public ResponseImpl trade(RequestImpl requestImpl){
        loggerStatic.log("\nTRADE\n");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, true, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> userID -> Trading Liste **/
        String userID = requestImpl.getAuth();

        /** --> User CHECK **/
        Player currentPlayer = this.playerHandler.getItemById(userID);

        /** --> TRADE ID **/
        String tradeID = "T-" + requestImpl.getPath().split("/tradings/")[1];
        String cardID = "C-" + requestImpl.getBody().replace("\"","");
        Trade currentTrade = trade.getItemById(tradeID);

        /** --> TRADE CHECK - USER != TRADE OWNER**/
        if (currentTrade == null) {
            //System.out.println(textColor.ANSI_RED + " TRADE NOT FOUND" + textColor.ANSI_RESET);
            loggerStatic.log("\nTRADE NOT FOUND\n");
            return new ResponseBuilderImpl().statusNotFound(tradeSerializer.message(" TRADE NOT FOUND").toString());
        }

        /** --> TRADE CHECK - USER != TRADE OWNER**/
        if (trade.checkTradeOwner(userID, currentTrade)) {
            //System.out.println(textColor.ANSI_RED + "NOT POSSIBLE TO TRADE" + textColor.ANSI_RESET);
            loggerStatic.log("\nNOT POSSIBLE TO TRADE\n");
            return new ResponseBuilderImpl().statusUnAuthorized(tradeSerializer.message("NOT POSSIBLE TO TRADE").toString());
        }

        if (!trade.checkTradeRequirement(currentTrade,cardID)) {
            // System.out.println(textColor.ANSI_RED + "NOT POSSIBLE TO TRADE" + textColor.ANSI_RESET);
            loggerStatic.log("\nNOT POSSIBLE TO TRADE\n");
            return new ResponseBuilderImpl().statusUnAuthorized(tradeSerializer.message("NOT POSSIBLE TO TRADE").toString());
        }

        cardHolderHandler.switchCardHolder(currentTrade.getUserID(),currentPlayer.getUserID(),currentTrade.getCard().getCardID(),cardID);

        //System.out.println(textColor.ANSI_GREEN + "TRADE - SUCCESS" + textColor.ANSI_RESET);
        loggerStatic.log("\n TRADE SUCCESS\n");
        return new ResponseBuilderImpl().statusOK(tradeSerializer.message("TRADE - SUCCESS").toString());
    }

    /** --> Löscht Trade **/
    public ResponseImpl delete(RequestImpl requestImpl) {
        loggerStatic.log("\nDELETE\n");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> userID -> Trading Liste **/
        String userID = requestImpl.getAuth();
        /** --> User CHECK **/
        Player currentPlayer = this.playerHandler.getItemById(userID);

        String tradeID = "T-" + requestImpl.getPath().split("/tradings/")[1];

        Trade currentTrade = trade.getItemById(tradeID);
        if (currentTrade == null) {
            //System.out.println(textColor.ANSI_RED + "DELETE - TRADE NOT FOUND" + textColor.ANSI_RESET);
            loggerStatic.log("\nDELETE - TRADE NOT FOUND\n");
            return new ResponseBuilderImpl().statusNotFound(tradeSerializer.message("DELETE - TRADE NOT FOUND").toString());
        }


        if (!trade.checkTradeOwner(currentPlayer.getUserID(), currentTrade)) {
            //System.out.println(textColor.ANSI_RED + "UNAUTHORIZED" + textColor.ANSI_RESET);
            loggerStatic.log("\nUNAUTHORIZED\n");
            return new ResponseBuilderImpl().statusUnAuthorized(tradeSerializer.message("UNAUTHORIZED").toString());
        }

        if (trade.delete(currentTrade)) {
            //System.out.println(textColor.ANSI_GREEN + "DELETE - SUCCESS" + textColor.ANSI_RESET);
            loggerStatic.log("\nDELETE - SUCCESS\n");
            return new ResponseBuilderImpl().statusOK(tradeSerializer.message("DELETE - SUCCESS").toString());
        }
        // System.out.println(textColor.ANSI_RED + "DELETE - FAILED" + textColor.ANSI_RESET);
        loggerStatic.log("\nDELETE - FAILED\n");
        return new ResponseBuilderImpl().statusNotFound(tradeSerializer.message("DELETE - FAILED").toString());
    }

}
