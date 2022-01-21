package at.technikum.repository;

import at.technikum.database.AbstractDBTable;
import at.technikum.model.repository.Player;
import at.technikum.model.repository.Profil;
import at.technikum.model.ProfilImpl;
import at.technikum.utils.TextColor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfilRepositoryImpl extends AbstractDBTable implements ProfilRepository {



    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public ProfilRepositoryImpl() {
        this.tableName = "\"profil\"";
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                            Builder                            **/
    /*******************************************************************/
    private Profil playerInfoBuilder(ResultSet result) {
        try {
            if (result.next()) {
                Profil playerInfo = ProfilImpl.builder()
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
    public Profil updateProfil(Profil playerInfo) {
        if (this.getItemById(playerInfo.getUserID()) == null) {
            this.insert(playerInfo);
        } else {
            this.update(playerInfo);
        }
        return getItemById(playerInfo.getUserID());
    }

    @Override
    public boolean createProfil(Player currentPlayer){
        PlayerRepository playerRepository = new PlayerRepositoryImpl();
        if(playerRepository.getItemById(currentPlayer.getUserID()) == null){
            return false;
        }
        Profil profil = ProfilImpl.builder()
                .userID(currentPlayer.getUserID())
                .name(currentPlayer.getUsername())
                .bio("Hello World!")
                .image(":]")
                .build();
        insert(profil);
        return true;
    }
    /**
     * GET PLAYER INFO BY ID
     **/
    @Override
    public Profil getItemById(String userID) {
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
    public Profil insert(Profil item) {
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

        return getItemById(item.getUserID());
    }

    /**
     * UPDATE CURRENT PLAYER INFO
     **/
    @Override
    public Profil update(Profil item) {
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

        return getItemById(item.getUserID());
    }

    /**
     * DELETE CURRENT PLAYER INFO
     **/
    @Override
    public boolean delete(Profil item) {
        //System.out.println(ANSI_BLUE + "#DELETE:" + ANSI_RESET);
        this.parameter = new String[]{};
        if (getItemById(item.getUserID()) == null) {
            return false;
        }
        this.setStatement("DELETE FROM " + this.tableName +
                        " WHERE user_id = '" + item.getUserID() + "';"
                , this.parameter);
        return true;
    }
}
