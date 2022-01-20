package at.technikum.serializer;

import at.technikum.model.Battle;
import at.technikum.model.DeckImpl;
import at.technikum.model.IDeck;
import at.technikum.model.Player;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class BattleSerializer extends Repository {
    public BattleSerializer() {
    }

    /** Wandelt Java Package-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    public JsonObject convertBattleToJson(Battle battle, boolean battleID, boolean player1, boolean player2, boolean round, boolean winner, boolean searching){
        JsonObject object = new JsonParser().parse(gson.toJson(battle, Battle.class)).getAsJsonObject();
        if(!battleID){object.remove("battleID");}
        if(!player1){object.remove("player1");}
        if(!player2){object.remove("player2");}
        if(!round){object.remove("round");}
        if(!winner){object.remove("winner");}
        if(!searching){object.remove("searching");}
        return object;
    } // TODO: 10.01.2022 Fertig

}
