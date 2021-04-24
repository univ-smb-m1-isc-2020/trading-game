package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Player {
    @Id
    @GeneratedValue
    protected long id;
    @ManyToOne
    protected TradingGameUser user;
    @OneToMany
    protected List<Portfolio> portfolios;

    public Player(){
        //JPA
    }

    public Player(TradingGameUser user, int portfolioCount, int initialBalance) {
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

    public TradingGameUser getUser() {
        return user;
    }

    public void setUser(TradingGameUser user) {
        this.user = user;
    }
}
