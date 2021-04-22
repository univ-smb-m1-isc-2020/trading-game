package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Ticker {
    private String name;
    @Id
    private String symbol;
    private boolean has_eod;
    private String country;

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
}
