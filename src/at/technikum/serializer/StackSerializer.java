package at.technikum.serializer;

import at.technikum.model.StackImpl;
import at.technikum.model.repository.Stack;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StackSerializer extends AbstractSerializer {
    public StackSerializer() {
    }

    /** Wandelt Java Package-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    public JsonObject convertStackToJson(Stack currentStack, boolean userID, boolean stack){
        JsonObject object = new JsonParser().parse(gson.toJson(currentStack, StackImpl.class)).getAsJsonObject();
        if(!userID){object.remove("userID");}
        if(!stack){object.remove("stack");}
        return object;
    } // TODO: 10.01.2022 Fertig
}
