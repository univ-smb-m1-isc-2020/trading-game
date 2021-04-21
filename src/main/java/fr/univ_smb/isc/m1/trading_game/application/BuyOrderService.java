package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.springframework.stereotype.Service;

@Service
public class BuyOrderService {
    private PortfolioService portfolioService;

    public boolean apply(BuyOrder order, EOD dayData, long portfolioId) {

        int buyingPrice = dayData.getClose();
        int quantity = order.getQuantity();
        String symbol = dayData.getSymbol().getSymbol();

        boolean success = portfolioService.buy(portfolioId, symbol, buyingPrice, quantity);

        if (success) {
            order.setPending(false);
            return true;
        } else {
            return false;
        }
    }
}
