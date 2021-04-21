package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

public class Player {
    protected final Object user;//TODO user class instead of Object
    protected final List<Portfolio> portfolios;
    protected final List<Order> orders;

    public int getTotalBalance(){
        int total = 0;
        for(Portfolio port: portfolios){
            total+= port.getBalance();
        }
        return total;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Player(Object user, int portfolioCount, int initialBalance) {
        this.user = user;
        this.portfolios = new ArrayList<>();
        this.orders = new ArrayList<>();
        for(int i=0; i<portfolioCount; i++){
            Portfolio port = new Portfolio(initialBalance);
            portfolios.add(port);
        }
    }

    public void addOrder(Order o) {
        orders.add(o);
    }

    public void applyOrders(EOD dayData) {
        for(Order o : orders){
            o.apply(dayData);
        }
    }
}
