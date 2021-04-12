package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;

import java.util.Date;

public class SellOrder extends Order {

    public SellOrder(Portfolio portfolio, Ticker ticker, int quantity) {
        super(portfolio, ticker, quantity);
    }

    @Override
    public void apply(EOD dayData) {
        if(dayData.symbol != this.ticker) return;

        int buyingPrice = dayData.close;
        if(portfolio.canSell(ticker, quantity)){
            portfolio.sell(dayData.symbol, quantity, buyingPrice*quantity);
        }
    }
}
