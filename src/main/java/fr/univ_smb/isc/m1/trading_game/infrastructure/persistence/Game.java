package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import javax.persistence.*;
import java.util.*;

@Entity
public class Game {
    @Id
    @GeneratedValue
    protected long id;
    protected int maxPortfolios;
    protected int initialBalance; //cents
    protected int transactionFee; //cents
    protected Date startDate;
    protected int totalDuration; // days

    protected int currentDuration; // days

    @ManyToMany
    protected List<Player> players;

    public int getMaxPortfolios() {
        return maxPortfolios;
    }

    public int getInitialBalance() {
        return initialBalance;
    }

    public int getTransactionFee() {
        return transactionFee;
    }

    public Date getStartDate() {
        return startDate;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public int getCurrentDuration() {
        return currentDuration;
    }

    public boolean hasEnded() {
        return currentDuration>=totalDuration;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Game(){
        //JPA
    }

    public Game(int maxPortfolios, int initialBalance, int transactionFee, Date startDate, int totalDuration) {
        this.maxPortfolios = maxPortfolios;
        this.initialBalance = initialBalance;
        this.transactionFee = transactionFee;
        this.startDate = startDate;
        this.totalDuration = totalDuration;

        currentDuration = 0;
        players = new ArrayList<>();
    }

    public void addPlayer(Player p){
        players.add(p);
    }

    public void applyDayData(List<EOD> dayData){
        for(EOD eod : dayData){
            for(Player p : players){
                p.applyOrders(eod);
            }
        }
        currentDuration++;
    }
}