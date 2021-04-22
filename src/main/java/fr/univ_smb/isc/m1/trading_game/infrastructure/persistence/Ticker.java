package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Ticker {
    private String name;
    @Id
    private String symbol;
    private boolean has_eod;
    private String country;
    @OneToOne
    @JoinColumn(name = "mic")
    private Exchange stock_exchange;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public boolean isHas_eod() {
        return has_eod;
    }

    public void setHas_eod(boolean has_eod) {
        this.has_eod = has_eod;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Exchange getStock_exchange() {
        return stock_exchange;
    }

    public void setStock_exchange(Exchange stock_exchange) {
        this.stock_exchange = stock_exchange;
    }
}
