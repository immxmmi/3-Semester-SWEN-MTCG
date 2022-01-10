package at.technikum.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Repository {

    protected Gson gson;
    public Repository(){
        this.gson = new Gson();
    }
    /** --> wandelt message String in JSON-Objekt um **/
    public JsonObject message(String message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Message",message);
        return jsonObject;
    } // TODO: 10.01.2022 Fertig
}
