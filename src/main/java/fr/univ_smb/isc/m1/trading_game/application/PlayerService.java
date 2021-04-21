package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    private PlayerRepository repository;
    private PortfolioService portfolioService;

    public void applyOrders(long playerId, EOD dayData){
        Player player = repository.getOne(playerId);
        List<Portfolio> portfolios = player.getPortfolios();
        for(Portfolio p : portfolios){
            portfolioService.applyOrders(p.getId(), dayData);
        }
    }
}
