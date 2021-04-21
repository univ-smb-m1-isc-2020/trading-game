package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import javax.persistence.Entity;

@Entity
public class BuyOrder extends Order {

    public BuyOrder() {
        super();
    }

    public BuyOrder(Portfolio portfolio, Ticker ticker, int quantity) {
        super(portfolio, ticker, quantity);
    }
}
