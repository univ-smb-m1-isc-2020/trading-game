package fr.univ_smb.isc.m1.trading_game.application;

import java.util.List;

public class TestablePlayer extends Player{
    public TestablePlayer(Object user, int portfolioCount, int initialBalance) {
        super(user, portfolioCount, initialBalance);
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order o) {
        orders.add(o);
    }
}
