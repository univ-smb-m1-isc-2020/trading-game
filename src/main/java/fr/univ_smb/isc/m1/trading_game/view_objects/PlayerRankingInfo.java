package fr.univ_smb.isc.m1.trading_game.view_objects;

public class PlayerRankingInfo {
    private final String playerName;
    private final int totalBalance;

    public PlayerRankingInfo(String playerName, int totalBalance) {
        this.playerName = playerName;
        this.totalBalance = totalBalance;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getTotalBalance() {
        return totalBalance;
    }
}
