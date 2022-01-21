package at.technikum.model.repository;

public interface Player {

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

    Stack getStack();

    Stack getFreeStack();

    IDeck getDeck();

    void setStack(Stack stack);

    void setFreeStack(Stack freeStack);

    void setDeck(IDeck deck);


}
