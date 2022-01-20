package at.technikum.model;

public interface Battle {
    String getBattleID();

    Player getPlayer1();

    Player getPlayer2();

    int getRound();

    Player getWinner();

    boolean isSearching();

    void setPlayer2(Player player2);

    void setRound(int round);

    void setWinner(Player winner);

    void setSearching(boolean searching);
}
