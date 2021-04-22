package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository repository;
    private final PortfolioService portfolioService;

    public PlayerService(PlayerRepository repository, PortfolioService portfolioService) {
        this.repository = repository;
        this.portfolioService = portfolioService;
    }

    public Player getPlayer(long playerId) {
        return repository.getOne(playerId);
    }

    public void applyOrders(long playerId, EOD dayData){
        Player player = repository.getOne(playerId);
        List<Portfolio> portfolios = player.getPortfolios();
        for(Portfolio p : portfolios){
            portfolioService.applyOrders(p.getId(), dayData);
        }
    }
}
