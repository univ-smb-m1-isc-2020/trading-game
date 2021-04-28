package fr.univ_smb.isc.m1.trading_game.view_objects;


import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;

public class PortfolioTickerInfo {
    private final String symbol;
    private final int quantity;
    private final double unitPrice;

    public PortfolioTickerInfo(Ticker t, int quantity, int unitPrice){
        this.symbol = t.getSymbol();
        this.quantity = quantity;
        this.unitPrice = unitPrice/100.0;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalPrice() {
        return getUnitPrice()*getQuantity();
    }
}