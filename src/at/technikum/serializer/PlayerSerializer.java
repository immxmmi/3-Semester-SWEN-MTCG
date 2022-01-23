package at.technikum.serializer;

import at.technikum.model.PlayerImpl;
import at.technikum.model.repository.Player;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PlayerSerializer extends AbstractSerializer {
    public PlayerSerializer() {
    }

    /** Wandelt Java Player-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    public JsonObject convertPlayerToJson(Player player, boolean userID, boolean Username, boolean Password, boolean coins, boolean status, boolean stack, boolean deck, boolean freeStack, boolean elo){
        JsonObject object = new JsonParser().parse(gson.toJson(player, PlayerImpl.class)).getAsJsonObject();
        if(!userID){object.remove("userID");}
        if(!Username){object.remove("Username");}
        if(!Password){object.remove("Password");}
        if(!coins){object.remove("coins");}
        if(!status){object.remove("status");}
        if(!stack){object.remove("stack");}
        if(!deck){object.remove("deck");}
        if(!freeStack){object.remove("freeStack");}
        if(!elo){object.remove("elo");}
        object.remove("tokenSupplier");
        object.remove("token");
        return object;
    } // TODO: 10.01.2022 Fertig

}
