package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;

import java.util.Date;

public class SellOrder extends Order{

    public SellOrder(Portfolio portfolio, Ticker ticker, int quantity) {
        super(portfolio, ticker, quantity);
    }

    @Override
    public void apply(Date date) {
        EOD dayData = new EOD();//TODO get it from db
        int buyingPrice = 50;//TODO get it from EOD dayData
        if(portfolio.canSell(ticker, quantity)){
            portfolio.sell(dayData.symbol, quantity, buyingPrice*quantity);
        }
    }
}
