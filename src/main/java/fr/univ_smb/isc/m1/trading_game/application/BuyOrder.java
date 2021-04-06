package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;

import java.util.Date;

public class BuyOrder extends Order{

    public BuyOrder(Portfolio portfolio, Ticker ticker, int quantity) {
        super(portfolio, ticker, quantity);
    }

    @Override
    public void apply(Date date) {
        EOD dayData = null;//TODO get it from db
        int buyingPrice = 50;//TODO get it from EOD dayData
        if(portfolio.canAfford(buyingPrice*quantity)){
            portfolio.buy(dayData.symbol, quantity, buyingPrice*quantity);
        }
    }
}
