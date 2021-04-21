package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Portfolio {
    @Id
    @GeneratedValue
    protected long id;

    protected int balance; //cents

    @ElementCollection
    protected Map<Ticker, Integer> parts;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getQuantity(Ticker ticker){
        return parts.getOrDefault(ticker, 0);
    }

    public void setQuantity(Ticker ticker, int value){
        parts.put(ticker, value);
    }

    public Portfolio(){
        //JPA
    }

    public Portfolio(int balance){
        this.balance = balance;
        parts = new HashMap<>();
    }
}
