package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import javax.persistence.*;

@MappedSuperclass
public abstract class Order {
    @Id
    @GeneratedValue
    protected int id;
    @ManyToOne
    protected Portfolio portfolio;
    @ManyToOne
    protected Ticker ticker;
    protected int quantity;
    protected boolean isPending;

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public Order(){
        //JPA
    }

    public Order(Portfolio portfolio, Ticker ticker, int quantity) {
        isPending = true;
        this.portfolio = portfolio;
        this.ticker = ticker;
        this.quantity = quantity;
    }

    public abstract void apply(EOD dayData);
}
