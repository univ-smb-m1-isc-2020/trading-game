package fr.univ_smb.isc.m1.trading_game.application;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Object user;//TODO user class instead of Object
    private List<Portfolio> portfolios;

    public Player(Object user, int portfolioCount, int initialBalance) {
        this.user = user;
        this.portfolios = new ArrayList<>();
        for(int i=0; i<portfolioCount; i++){
            Portfolio port = new Portfolio(initialBalance);
            portfolios.add(port);
        }
    }
}
