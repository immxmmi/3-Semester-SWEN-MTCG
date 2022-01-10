package at.technikum.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(toBuilder = true)
public class ProfilImpl implements Profil {

    @Getter
    @Setter
    @SerializedName("userID")
    private String userID;
    @Getter
    @SerializedName("Name")
    private String name;
    @Getter
    @SerializedName("Bio")
    private String bio;
    @Getter
    @SerializedName("Image")
    private String image;

}