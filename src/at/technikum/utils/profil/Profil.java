package at.technikum.utils.profil;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(toBuilder = true)
public class Profil implements IProfil {

    @Getter
    @Setter
    @SerializedName("userID")
    String userID;
    @Getter
    @SerializedName("Name")
    String name;
    @Getter
    @SerializedName("Bio")
    String bio;
    @Getter
    @SerializedName("Image")
    String image;

}