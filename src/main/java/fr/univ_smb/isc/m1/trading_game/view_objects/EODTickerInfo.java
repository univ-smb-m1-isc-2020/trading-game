package fr.univ_smb.isc.m1.trading_game.view_objects;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;

public class EODTickerInfo {
    private final String symbol;
    private final double unitPrice;

    public EODTickerInfo(Ticker t, int unitPrice){
        this.symbol = t.getSymbol();
        this.unitPrice = unitPrice/100.0;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
}