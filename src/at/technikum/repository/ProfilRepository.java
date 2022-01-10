package at.technikum.repository;

import at.technikum.database.AbstractDBTable;
import at.technikum.model.IProfil;
import at.technikum.model.Profil;
import at.technikum.utils.tools.TextColor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfilRepository extends AbstractDBTable implements IProfilRepository {


    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public ProfilRepository() {
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

            System.out.println(TextColor.ANSI_RED + "GETOBJECT -ERRROR: " + e + TextColor.ANSI_RESET);
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
