package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Player {
    @Id
    @GeneratedValue
    protected long id;
    protected Object user;//TODO user class instead of Object
    @OneToMany
    protected List<Portfolio> portfolios;

    public Player(){
        //JPA
    }

    public Player(Object user, int portfolioCount, int initialBalance) {
        this.user = user;
        this.portfolios = new ArrayList<>();
        for(int i=0; i<portfolioCount; i++){
            Portfolio port = new Portfolio(initialBalance);
            portfolios.add(port);
        }
    }

    public long getId() {
        return id;
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
    }
}
