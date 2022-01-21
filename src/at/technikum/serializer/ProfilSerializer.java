package at.technikum.serializer;

import at.technikum.model.repository.Profil;
import at.technikum.model.ProfilImpl;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ProfilSerializer extends AbstractSerializer {

    public ProfilSerializer(){}

    /** Wandelt Java Player-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    public JsonObject convertProfilToJson(Profil profil, boolean userID, boolean name, boolean bio, boolean image){
        JsonObject object = new JsonParser().parse(gson.toJson(profil, ProfilImpl.class)).getAsJsonObject();
        if(!userID){object.remove("userID");}
        if(!name){object.remove("Name");}
        if(!bio){object.remove("Bio");}
        if(!image){object.remove("Image");}
        return object;
    } // TODO: 10.01.2022 Fertig

}
