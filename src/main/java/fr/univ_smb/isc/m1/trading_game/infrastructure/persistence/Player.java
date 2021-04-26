package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Player {
    @Id
    @GeneratedValue
    protected long id;
    @ManyToOne
    protected TradingGameUser user;
    @OneToMany(fetch = FetchType.EAGER)
    protected Set<Portfolio> portfolios;

    public Player(){
        //JPA
    }

    public Player(TradingGameUser user, Set<Portfolio> portfolios) {
        this.user = user;
        this.portfolios = portfolios;
    }

    public long getId() {
        return id;
    }

    public Set<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(Set<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    public TradingGameUser getUser() {
        return user;
    }

    public void setUser(TradingGameUser user) {
        this.user = user;
    }
}
