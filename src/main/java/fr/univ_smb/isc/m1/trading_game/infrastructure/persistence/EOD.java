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

    public int open;
    public int high;
    public int low;
    public int close;
    public int volume;
}
