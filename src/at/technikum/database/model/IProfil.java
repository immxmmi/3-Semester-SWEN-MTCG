package at.technikum.database.model;

public interface IProfil {
    String getUserID();

    String getName();

    String getBio();

    String getImage();

    void setUserID(String userID);
}
