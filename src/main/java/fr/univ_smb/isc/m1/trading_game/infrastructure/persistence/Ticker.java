package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Ticker {
    public String name;
    @Id
    public String symbol;
}
