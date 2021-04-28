package fr.univ_smb.isc.m1.trading_game.view_objects;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Game;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Player;

import java.util.*;

public class GameInfo {
    private final Game game;
    private final List<PlayerRankingInfo> rankingInfos;

    public GameInfo(Game game, List<PlayerRankingInfo> rankingInfos) {
        this.game = game;
        this.rankingInfos = rankingInfos;
    }

    public List<PlayerRankingInfo> getRankingInfos() {
        return rankingInfos;
    }

    public int getCurrentDuration(){
        return game.getCurrentDuration();
    }

    public int getTotalDuration(){
        return game.getTotalDuration();
    }

    public int getPlayerCount(){
        return game.getPlayerCount();
    }

    public int getMaxPortfolios(){
        return game.getMaxPortfolios();
    }

    public int getInitialBalance(){
        return game.getInitialBalance();
    }

    public int getTransactionFee(){
        return game.getTransactionFee();
    }

    public Date getStartDate(){
        return game.getStartDate();
    }
}
