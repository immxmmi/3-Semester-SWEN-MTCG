package profil;

import at.technikum.handler.PlayerHandlerImpl;
import at.technikum.handler.ProfilHandlerImpl;
import at.technikum.handler.repository.PlayerHandler;
import at.technikum.model.PlayerImpl;
import at.technikum.model.ProfilImpl;
import at.technikum.model.repository.Player;
import at.technikum.model.repository.Profil;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class ProfilHandlerImplTest {

    @Mock
    ProfilHandlerImpl profilHandler = new ProfilHandlerImpl();
    private Player createTestUser(String letter){
        return PlayerImpl.builder()
                .userID("Token"+letter)
                .username("TestUser"+letter)
                .password("pw")
                .build();

    }
    private Player getTestUser(Player player){
        PlayerHandler playerHandler = new PlayerHandlerImpl();
        assertNotNull(player);
        playerHandler.register(player.getUserID(),player.getUsername(),player.getPassword());
        assertNotNull(playerHandler.getPlayerByUsername(player.getUsername()));
        assertNotNull(playerHandler.getItemById(player.getUserID()));
        return player;
    }



    @Test
    void updateProfil() {
        Player player = getTestUser(createTestUser("Profil"));
        assertTrue(profilHandler.createProfil(player));
        Profil profil = profilHandler.getItemById(player.getUserID());
        assertNotNull(profil);
        Profil newProfil = ProfilImpl.builder()
                .userID(profil.getUserID())
                .name("TestName")
                .bio("TestBio")
                .image(":}")
                .build();

        assertNotNull(profil);
        profil = profilHandler.updateProfil(newProfil);
        assertNotNull(profil);
        assertEquals(profil.getName(),"TestName");
        assertEquals(profil.getBio(),"TestBio");
        assertEquals(profil.getImage(),":}");
        delete(profil);
    }

    @Test
    void createProfil() {
        Player player = getTestUser(createTestUser("Profil"));
        assertTrue(profilHandler.createProfil(player));
        Profil profil = profilHandler.getItemById(player.getUserID());
        assertNotNull(profil);
        delete(profil);
    }


    @Test
    void delete(Profil profil) {
        PlayerHandler playerHandler = new PlayerHandlerImpl();
        profilHandler.delete(profil);
        playerHandler.delete(playerHandler.getItemById(profil.getUserID()));
    }
}