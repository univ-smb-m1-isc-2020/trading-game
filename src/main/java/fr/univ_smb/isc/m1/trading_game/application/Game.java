package fr.univ_smb.isc.m1.trading_game.application;

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
    private List<Player> players;

    public Game(int maxPortfolios, int initialBalance, int transactionFee, Date startDate, int totalDuration) {
        this.maxPortfolios = maxPortfolios;
        this.initialBalance = initialBalance;
        this.transactionFee = transactionFee;
        this.startDate = startDate;
        this.totalDuration = totalDuration;

        currentDuration = 0;
        players = new ArrayList<>();
    }
}