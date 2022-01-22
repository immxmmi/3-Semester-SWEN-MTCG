package at.technikum.serializer;

import at.technikum.model.BattleImpl;
import at.technikum.model.repository.Battle;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BattleSerializer extends AbstractSerializer {
    public BattleSerializer() {
    }

    /** Wandelt Java Package-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    public JsonObject convertBattleToJson(Battle battle, boolean battleID, boolean player1, boolean player2, boolean round, boolean winner, boolean searching){
        JsonObject object = new JsonParser().parse(gson.toJson(battle, BattleImpl.class)).getAsJsonObject();
        if(!battleID){object.remove("battleID");}
        if(!player1){object.remove("player1");}
        if(!player2){object.remove("player2");}
        if(!round){object.remove("round");}
        if(!winner){object.remove("winner");}
        if(!searching){object.remove("searching");}
        return object;
    } // TODO: 10.01.2022 Fertig

}
