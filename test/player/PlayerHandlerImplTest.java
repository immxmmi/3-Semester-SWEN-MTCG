package player;

import at.technikum.handler.PlayerHandlerImpl;
import at.technikum.model.PlayerImpl;
import at.technikum.model.repository.Player;
import at.technikum.utils.Tools;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PlayerHandler")
class PlayerHandlerImplTest {

    @Mock
    PlayerHandlerImpl playerHandler = new PlayerHandlerImpl();

    private Player createTestUser(String letter){
        return PlayerImpl.builder()
                .userID("Token"+letter)
                .username("TestUser"+letter)
                .password("pw")
                .build();

    }
    private Player getTestUser(Player player){
        assertNotNull(player);
        playerHandler.register(player.getUserID(),player.getUsername(),player.getPassword());
        assertNotNull(playerHandler.getPlayerByUsername(player.getUsername()));
        assertNotNull(playerHandler.getItemById(player.getUserID()));
        return player;
    }


    @Test
    void addCoins(){
        Player player = createTestUser("addCoins");
        player = getTestUser(player);
        double currentCoins = player.getCoins();
        playerHandler.addCoins(player,2000);
        player = getTestUser(player);
        assertEquals(player.getCoins(),currentCoins + 2000);
        delete(player);

    }
    @Test
    void giveCoins() {
        Player player = createTestUser("giveCoins");

        player = getTestUser(player);

        double currentCoins = player.getCoins();
        playerHandler.giveCoins(player,1);
        player = getTestUser(player);
        assertEquals(player.getCoins(),currentCoins - 1);
        delete(player);
    }
    @Test
    void register() {
        Player player = createTestUser("register");
        assertNotNull(player);

        //Register
        playerHandler.register(player.getUserID(),player.getUsername(),player.getPassword());
        assertNotNull(playerHandler.getPlayerByUsername(player.getUsername()));
        assertNotNull(playerHandler.getItemById(player.getUserID()));

        delete(player);
    }
    @Test
    void loginLogout(){
        Player player = createTestUser("login");

        player = getTestUser(player);
        String username = player.getUsername();
        String pw = player.getPassword();

        // Login
        assertEquals(playerHandler.login("testUser","false"),null);
        assertEquals(playerHandler.login(username,"false"),null);
        assertEquals(player.isStatus(),false);
        player = playerHandler.login(username,pw);
        assertNotNull(player);
        assertEquals(player.isStatus(),true);
        // Logout
        playerHandler.logout(player);
        player = (playerHandler.getPlayerByUsername(player.getUsername()));
        assertEquals(player.isStatus(),false);

        delete(player);
    }

    @Test
    void authorizePassword() {
        Tools tools = new Tools();
        String password = "test";
        String hashedPassword = tools.hashString(password);
        assertTrue(playerHandler.authorizePassword(hashedPassword,password));
        assertFalse(playerHandler.authorizePassword("pwTest",password));
    }



    private void delete(Player player){
        // DELETE
        assertNotNull(playerHandler.getPlayerByUsername(player.getUsername()));
        playerHandler.delete(player);
        assertEquals(playerHandler.getItemById(player.getUserID()),null);
    }


}