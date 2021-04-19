package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Game {
    protected final int maxPortfolios;
    protected final int initialBalance; //cents
    protected final int transactionFee; //cents
    protected final Date startDate;
    protected final int totalDuration; // days

    protected int currentDuration; // days
    protected final List<Player> players;

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

    public void applyEod(List<EOD> dayData){
        for(EOD eod : dayData){
            for(Player p : players){
                p.applyOrders(eod);
            }
        }
        currentDuration++;
    }
}