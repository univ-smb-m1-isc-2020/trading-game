package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Player {
    protected final Object user;//TODO user class instead of Object
    protected final List<Portfolio> portfolios;
    protected final List<Order> orders;

    public Player(Object user, int portfolioCount, int initialBalance) {
        this.user = user;
        this.portfolios = new ArrayList<>();
        this.orders = new ArrayList<>();
        for(int i=0; i<portfolioCount; i++){
            Portfolio port = new Portfolio(initialBalance);
            portfolios.add(port);
        }
    }

    public void createBuyOrder(int portfolioNumber, Ticker ticker, int quantity) {
        if (portfolioNumber < portfolios.size() && portfolioNumber > 0) {
            BuyOrder o = new BuyOrder(portfolios.get(portfolioNumber), ticker, quantity);
            orders.add(o);
        }
    }

    public void createSellOrder(int portfolioNumber, Ticker ticker, int quantity){
        if (portfolioNumber < portfolios.size() && portfolioNumber > 0) {
            SellOrder o = new SellOrder(portfolios.get(portfolioNumber), ticker, quantity);
            orders.add(o);
        }
    }

    public void applyOrders(EOD dayData) {
        for(Order o : orders){
            o.apply(dayData);
        }
    }

    public int getTotalBalance(){
        int total = 0;
        for(Portfolio port: portfolios){
            total+= port.getBalance();
        }
        return total;
    }
}
