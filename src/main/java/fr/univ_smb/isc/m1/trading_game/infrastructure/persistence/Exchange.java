package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Exchange {
    public String name;
    public String acronym;
    @Id
    public String mic;
    public String country;
    public String country_code;
    public String city;
    public String website;
}
