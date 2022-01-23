package at.technikum.serializer;

import at.technikum.model.TradeImpl;
import at.technikum.model.repository.Trade;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TradeSerializer extends AbstractSerializer {

    /** Wandelt Java Package-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    public JsonObject convertTradeToJson(Trade trade, boolean tradeID, boolean userID, boolean card, boolean minPower, boolean cardTyp){
        JsonObject object = new JsonParser().parse(gson.toJson(trade, TradeImpl.class)).getAsJsonObject();
        if(!tradeID){object.remove("tradeID");}
        if(!userID){object.remove("userID");}
        if(!card){object.remove("card");}
        if(!minPower){object.remove("minPower");}
        if(!cardTyp){object.remove("cardTyp");}
        return object;

    } // TODO: 10.01.2022 Fertig


}
