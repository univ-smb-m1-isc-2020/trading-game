package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.springframework.stereotype.Service;

@Service
public class BuyOrderService {
    private final BuyOrderRepository repository;
    private final PortfolioService portfolioService;

    public BuyOrderService(BuyOrderRepository repository, PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
        this.repository = repository;
    }

    public boolean apply(long orderId, EOD dayData, long portfolioId) {
        BuyOrder order = repository.getOne(orderId);

        if(isNotApplicable(order, dayData)) return false;

        int buyingPrice = dayData.getClose();
        int quantity = order.getQuantity();
        String symbol = dayData.getSymbol().getSymbol();

        boolean success = portfolioService.buy(portfolioId, symbol, buyingPrice, quantity);

        if (success) {
            order.setPending(false);
            repository.save(order);
            return true;
        } else {
            return false;
        }
    }

    private boolean isNotApplicable(Order order, EOD dayData) {
        return !order.isPending() || dayData.getSymbol() != order.getTicker();
    }
}
