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

    public Date date;

    @ManyToOne
    @JoinColumn(name = "symbol")
    public Ticker symbol;

    public int open;
    public int high;
    public int low;
    public int close;
    public int volume;
}
