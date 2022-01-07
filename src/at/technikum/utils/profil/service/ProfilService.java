package at.technikum.utils.profil.service;

import at.technikum.database.AbstractDBTable;
import at.technikum.utils.profil.IProfil;
import at.technikum.utils.profil.Profil;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfilService extends AbstractDBTable implements IProfilService {

    @Getter
    private static ProfilService instance;


    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public ProfilService() {
        this.tableName = "\"playerInfo\"";
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                            Builder                            **/
    /*******************************************************************/
    private IProfil playerInfoBuilder(ResultSet result) {
        try {
            if (result.next()) {
                IProfil playerInfo = Profil.builder()
                        .userID(result.getString("user_id"))
                        .name(result.getString("name"))
                        .bio(result.getString("bio"))
                        .image(result.getString("image"))
                        .build();

                this.closeStatement();

                return playerInfo;
            }
        } catch (SQLException e) {

            System.out.println(ANSI_RED + "GETOBJECT -ERRROR: " + e + ANSI_RESET);
            e.printStackTrace();
        }
        this.closeStatement();
        return null;
    }

    /*******************************************************************/

    @Override
    public IProfil setInfo(IProfil playerInfo) {
        if (this.getInfoByID(playerInfo.getUserID()) == null) {
            this.insert(playerInfo);
        } else {
            this.update(playerInfo);
        }
        return getInfoByID(playerInfo.getUserID());
    }

    @Override
    public void printPlayerInfo(IProfil playerInfo) {
        if (playerInfo == null) {
            System.out.println(ANSI_RED + "NO PLAYER INFO" + ANSI_RESET);
            return;
        }
        int maxName = 31;
        int maxBio = 32;
        int maxImg = 30;
        System.out.println("###############################################");
        System.out.println("#                 PLAYER INFO                 #");
        System.out.println("###############################################");
        System.out.println("#                                             #");
        System.out.println("#       NAME: " + ANSI_BLUE + playerInfo.getName() + checkSpace(playerInfo.getName(), maxName) + ANSI_RESET + "#");
        System.out.println("#       BIO: " + ANSI_BLUE + playerInfo.getBio() + checkSpace(playerInfo.getBio(), maxBio) + ANSI_RESET + "#");
        System.out.println("#       IMAGE: " + ANSI_BLUE + playerInfo.getImage() + checkSpace(playerInfo.getImage(), maxImg) + ANSI_RESET + "#");
        System.out.println("#                                             #");
        System.out.println("###############################################");
    }

    /**
     * GET PLAYER INFO BY ID
     **/
    @Override
    public IProfil getInfoByID(String userID) {
        this.parameter = new String[]{userID};
        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE user_id = ? " + ";",
                this.parameter
        );
        return playerInfoBuilder(this.result);
    }

    /**
     * INSERT NEW PLAYER INFO
     **/
    @Override
    public IProfil insert(IProfil item) {
        //System.out.println(ANSI_BLUE + "#INSERT:" + ANSI_RESET);
        if (item == null) {
            return null;
        }
        this.parameter = new String[]{
                "" + item.getUserID(),
                "" + item.getName(),
                "" + item.getBio(),
                "" + item.getImage()
        };
        this.setStatement("INSERT INTO " + this.tableName + " (user_id,name,bio,image)VALUES(?,?,?,?);", this.parameter);

        return getInfoByID(item.getUserID());
    }

    /**
     * UPDATE CURRENT PLAYER INFO
     **/
    @Override
    public IProfil update(IProfil item) {
        //System.out.println(ANSI_BLUE + "#UPDATE:" + ANSI_RESET);
        if (item == null) {
            return null;
        }
        this.parameter = new String[]{
                "" + item.getName(),
                "" + item.getBio(),
                "" + item.getImage()
        };


        this.setStatement(
                "UPDATE " + this.tableName +
                        " SET name = ?, bio = ?, image = ? " +
                        "WHERE user_id = '" + item.getUserID() + "' ;"
                , this.parameter
        );

        return getInfoByID(item.getUserID());
    }

    /**
     * DELETE CURRENT PLAYER INFO
     **/
    @Override
    public boolean delete(IProfil item) {
        //System.out.println(ANSI_BLUE + "#DELETE:" + ANSI_RESET);
        this.parameter = new String[]{};
        if (getInfoByID(item.getUserID()) == null) {
            return false;
        }
        this.setStatement("DELETE FROM " + this.tableName +
                        " WHERE user_id = '" + item.getUserID() + "';"
                , this.parameter);
        return true;
    }
}
