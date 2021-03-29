package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Ticker {
    public String name;

    @Id
    public String symbol;

    public boolean has_eod;
    public String country;

    @OneToOne
    @JoinColumn(name = "mic")
    public Exchange stock_exchange;

}
