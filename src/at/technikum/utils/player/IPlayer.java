package at.technikum.utils.player;

public interface IPlayer {

    String getUserID();

    String getUsername();

    String getPassword();

    double getElo();

    double getCoins();

    boolean isStatus();

    void setStatus(boolean status);

    void setToken(String token);

    void setCoins(double coins);

    void setElo(double elo);

    //IStack getStack();

    //IStack getFreeStack();

    //IDeck getDeck();

    //void setStackDeck(IStack stack, IStack freeStack, IDeck deck);

    boolean authorizePassword(String password);


}
