package at.technikum.serializer;

import at.technikum.model.repository.Package;
import at.technikum.model.PackageImpl;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PackageSerializer extends AbstractSerializer {
    public PackageSerializer() {
    }

    /** Wandelt Java Package-Objekt in Json-Objekt um + filtert die einzelnen Elemente **/
    public JsonObject convertPackageToJson(Package packages, boolean packageID, boolean cards, boolean price, boolean date){
        JsonObject object = new JsonParser().parse(gson.toJson(packages, PackageImpl.class)).getAsJsonObject();
        if(!packageID){object.remove("packageID");}
        if(!cards){object.remove("cards");}
        if(!price){object.remove("price");}
        if(!date){object.remove("date");}
        return object;
    } // TODO: 10.01.2022 Fertig


}
