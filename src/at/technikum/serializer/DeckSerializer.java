package at.technikum.serializer;

import at.technikum.model.DeckImpl;
import at.technikum.model.IDeck;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DeckSerializer extends Repository{
    public DeckSerializer() {
    }

    /** Wandelt Java Package-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    public JsonObject convertDeckToJson(IDeck currentDeck, boolean userID, boolean deck, boolean cardIDList){
        JsonObject object = new JsonParser().parse(gson.toJson(currentDeck, DeckImpl.class)).getAsJsonObject();
        if(!userID){object.remove("userID");}
        if(!deck){object.remove("deck");}
        if(!cardIDList){object.remove("cardIDList");}
        return object;
    } // TODO: 10.01.2022 Fertig

}
