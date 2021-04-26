package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Portfolio {
    @Id
    @GeneratedValue
    protected long id;
    protected int balance; //cents
    @ElementCollection(fetch = FetchType.EAGER)
    protected Map<String, Integer> parts;
    @OneToMany(fetch = FetchType.EAGER)
    protected Set<Order> orders;

    public Portfolio(){
        //JPA
    }

    public Portfolio(int balance){
        this.balance = balance;
        parts = new HashMap<>();
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public int getQuantity(String ticker){
        return parts.getOrDefault(ticker, 0);
    }

    public void setQuantity(Ticker ticker, int value){
        parts.put(ticker.getSymbol(), value);
    }

    public Map<String, Integer> getParts() {
        return parts;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
