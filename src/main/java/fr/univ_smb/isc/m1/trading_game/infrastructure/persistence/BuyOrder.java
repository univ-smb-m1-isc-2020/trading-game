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

    @Override
    public void apply(EOD dayData) {
        if(dayData.getSymbol() != this.ticker) return;
        if(!isPending()) return;

        int buyingPrice = dayData.getClose();
        //portfolio.buy(dayData.getSymbol(), quantity, buyingPrice*quantity);
        setPending(false);
    }
}
