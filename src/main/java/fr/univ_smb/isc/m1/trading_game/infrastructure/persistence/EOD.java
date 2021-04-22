package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"symbol"})
@Entity
public class EOD {
    @Id
    @GeneratedValue
    private Long id;
    private Date date;
    @ManyToOne
    @JoinColumn(name = "symbol")
    private Ticker symbol;
    private int open;
    private int high;
    private int low;
    private int close;
    private int volume;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Ticker getSymbol() {
        return symbol;
    }

    public void setSymbol(Ticker symbol) {
        this.symbol = symbol;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getClose() {
        return close;
    }

    public void setClose(int close) {
        this.close = close;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
