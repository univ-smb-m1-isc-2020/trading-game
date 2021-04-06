package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;

import java.util.Date;

public abstract class Order {

    protected Portfolio portfolio;
    protected Ticker ticker;
    protected int quantity;

    public Order(Portfolio portfolio, Ticker ticker, int quantity) {
        this.portfolio = portfolio;
        this.ticker = ticker;
        this.quantity = quantity;
    }

    public abstract void apply(Date date);
}
