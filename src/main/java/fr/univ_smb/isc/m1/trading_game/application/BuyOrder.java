package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;

import java.util.Date;

public class BuyOrder extends Order {

    public BuyOrder(Portfolio portfolio, Ticker ticker, int quantity) {
        super(portfolio, ticker, quantity);
    }

    @Override
    public void apply(EOD dayData) {
        if(dayData.getSymbol() != this.ticker) return;
        if(!isPending()) return;

        int buyingPrice = dayData.getClose();
        if(portfolio.canAfford(buyingPrice*quantity)){
            portfolio.buy(dayData.getSymbol(), quantity, buyingPrice*quantity);
            setPending(false);
        }
    }
}
