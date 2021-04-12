package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Game {
    private final int maxPortfolios;
    private final int initialBalance; //cents
    private final int transactionFee; //cents
    private final Date startDate;
    private final int totalDuration; // days

    private int currentDuration; // days
    private final List<Player> players;

    public Game(int maxPortfolios, int initialBalance, int transactionFee, Date startDate, int totalDuration) {
        this.maxPortfolios = maxPortfolios;
        this.initialBalance = initialBalance;
        this.transactionFee = transactionFee;
        this.startDate = startDate;
        this.totalDuration = totalDuration;

        currentDuration = 0;
        players = new ArrayList<>();
    }

    public void createPlayer(Object user, int portfolioCount){
        Player p = new Player(user, portfolioCount, initialBalance);
    }

    public void applyEod(){
        List<EOD> dayData = null;// TODO get appropriate EOD
        for(EOD eod : dayData){
            for(Player p : players){
                p.applyOrders(eod);
            }
        }
        currentDuration++;
    }
}