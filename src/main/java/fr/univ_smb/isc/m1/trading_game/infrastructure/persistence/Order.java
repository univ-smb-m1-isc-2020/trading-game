package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Order {
    @Id
    @GeneratedValue
    protected long id;

    @ManyToOne
    protected Ticker ticker;
    protected int quantity;
    protected boolean isPending;

    public Order(){
        //JPA
    }

    public Order(Ticker ticker, int quantity) {
        isPending = true;
        this.ticker = ticker;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
