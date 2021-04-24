package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository repository;
    private final PortfolioService portfolioService;

    public PlayerService(PlayerRepository repository, PortfolioService portfolioService) {
        this.repository = repository;
        this.portfolioService = portfolioService;
    }

    public Player createPlayer(TradingGameUser user, int portfolioCount, int initialBalance){//TODO test
        List<Portfolio> portfolios = new ArrayList<>();
        for(int i=0; i<portfolioCount; i++){
            Portfolio p = portfolioService.create(initialBalance);
            portfolios.add(p);
        }
        Player p = new Player(user, portfolios);
        repository.save(p);
        return p;
    }

    public Player getPlayer(long playerId) {
        return repository.findById(playerId).orElse(null);
    }

    public List<Portfolio> getPortfolios(long playerId){
        Player player = repository.findById(playerId).orElse(null);//TODO test
        if(player==null) return new ArrayList<>();
        return player.getPortfolios();
    }

    public int getTotalBalance(long playerId){
        Player player = repository.findById(playerId).orElse(null);
        if(player==null) return 0;
        int total = 0;
        for(Portfolio port: player.getPortfolios()){
            total+= port.getBalance();
        }
        return total;
    }

    public void applyOrders(long playerId, EOD dayData){
        Player player = repository.findById(playerId).orElse(null);
        if(player==null) return;
        List<Portfolio> portfolios = player.getPortfolios();
        for(Portfolio p : portfolios){
            portfolioService.applyOrders(p.getId(), dayData);
        }
    }
}
