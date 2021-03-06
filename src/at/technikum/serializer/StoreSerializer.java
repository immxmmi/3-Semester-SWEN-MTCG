package at.technikum.serializer;

import at.technikum.model.StoreImpl;
import at.technikum.model.repository.Store;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StoreSerializer extends AbstractSerializer {
    public StoreSerializer() {
    }

    /** Wandelt Java Package-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    public JsonObject convertTransactionToJson(Store transaction, boolean transactionID, boolean sellerID, boolean itemID, boolean card, boolean price, boolean timeStamp){
        JsonObject object = new JsonParser().parse(gson.toJson(transaction, StoreImpl.class)).getAsJsonObject();
        if(!transactionID){object.remove("transactionID");}
        if(!sellerID){object.remove("sellerID");}
        if(!itemID){object.remove("itemID");}
        if (!card){object.remove("card");}
        if(!price){object.remove("price");}
        if(!timeStamp){object.remove("timeStamp");}
        return object;
    } // TODO: 10.01.2022 Fertig

}
