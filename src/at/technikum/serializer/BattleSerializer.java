package at.technikum.serializer;

import at.technikum.model.BattleImpl;
import at.technikum.model.repository.Battle;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BattleSerializer extends AbstractSerializer {
    public BattleSerializer() {
    }

    /** Wandelt Java Package-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    public JsonObject convertBattleToJson(Battle battle, boolean battleID, boolean player1, boolean player2, boolean round, boolean winner, boolean searching){
        JsonObject object = new JsonParser().parse(gson.toJson(battle, BattleImpl.class)).getAsJsonObject();
        JsonElement text = new JsonParser().parse("text");
        if(!battleID){object.remove("battleID");}

        //Player 1
        if(!player1){object.remove("player1");}else {
            object.remove("player1");
            text = new JsonParser().parse(battle.getPlayer1().getUsername());
            object.add("player1",text);
        }
        //Player 2
        if(!player2){object.remove("player2");}else {
            object.remove("player2");
            text = new JsonParser().parse(battle.getPlayer2().getUsername());
            object.add("player2",text);
        }

        if(!round){object.remove("round");}

        if(!winner){object.remove("winner");}else {
            object.remove("winner");
            if (battle.getWinner() != null){
                text =  new JsonParser().parse(battle.getWinner().getUsername());
            }else{
                text =  new JsonParser().parse("NoWinner");
            }
            object.add("Winner",text);
        }
        if(!searching){object.remove("searching");}
        return object;
    } // TODO: 10.01.2022 Fertig

}
