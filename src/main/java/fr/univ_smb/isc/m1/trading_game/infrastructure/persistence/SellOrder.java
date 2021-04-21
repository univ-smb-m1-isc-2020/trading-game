package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
public class SellOrder extends Order {

    public SellOrder() {
        super();
    }

    public SellOrder(Portfolio portfolio, Ticker ticker, int quantity) {
        super(portfolio, ticker, quantity);
    }
}
