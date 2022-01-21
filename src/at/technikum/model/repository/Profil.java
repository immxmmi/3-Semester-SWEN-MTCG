package at.technikum.model.repository;

public interface Profil {
    String getUserID();

    String getName();

    String getBio();

    String getImage();

    void setUserID(String userID);
}
