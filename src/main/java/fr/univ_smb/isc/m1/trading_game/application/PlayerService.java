package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Order;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Player;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Portfolio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    private PortfolioService portfolioService;

    public void applyOrders(long playerId, EOD dayData){
        Player player = new Player();//TODO get from repository
        List<Portfolio> portfolios = player.getPortfolios();
        for(Portfolio p : portfolios){
            portfolioService.applyOrders(p.getId(), dayData);
        }
    }
}
