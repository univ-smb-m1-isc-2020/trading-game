package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
public class Portfolio {
    @Id
    @GeneratedValue
    protected long id;

    protected int balance; //cents

    @ElementCollection
    protected Map<Ticker, Integer> parts;

    @OneToMany
    protected List<Order> orders;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public int getQuantity(Ticker ticker){
        return parts.getOrDefault(ticker, 0);
    }

    public void setQuantity(Ticker ticker, int value){
        parts.put(ticker, value);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Portfolio(){
        //JPA
    }

    public Portfolio(int balance){
        this.balance = balance;
        parts = new HashMap<>();
    }
}
