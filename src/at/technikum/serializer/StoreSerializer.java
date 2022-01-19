package at.technikum.serializer;

import at.technikum.model.Stack;
import at.technikum.model.StackImpl;
import at.technikum.model.Store;
import at.technikum.model.StoreImpl;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class StoreSerializer extends Repository{
    public StoreSerializer() {
    }

    /** Wandelt Java Package-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    public JsonObject convertTransactionToJson(Store transaction, boolean transactionID, boolean sellerID, boolean itemID, boolean price, boolean timeStamp){
        JsonObject object = new JsonParser().parse(gson.toJson(transaction, StoreImpl.class)).getAsJsonObject();
        if(!transactionID){object.remove("transactionID");}
        if(!sellerID){object.remove("sellerID");}
        if(!itemID){object.remove("itemID");}
        if(!price){object.remove("price");}
        if(!timeStamp){object.remove("timeStamp");}

        return object;

    } // TODO: 10.01.2022 Fertig

}
