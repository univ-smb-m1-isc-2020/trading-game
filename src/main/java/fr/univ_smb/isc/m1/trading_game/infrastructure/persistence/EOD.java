package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import javax.persistence.*;
import java.util.Date;

@Entity
public class EOD {
    @Id
    @GeneratedValue
    private Long id;

    public Date date;

    @ManyToOne
    @JoinColumn(name = "symbol")
    public Ticker symbol;

    @ManyToOne
    @JoinColumn(name = "mic")
    public Exchange exchange;

    public float open;
    public float high;
    public float low;
    public float close;
    public float volume;
    public float adj_open;
    public float adj_high;
    public float adj_low;
    public float adj_close;
    public float adj_volume;
}
