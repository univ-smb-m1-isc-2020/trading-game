package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.springframework.stereotype.Service;

@Service
public class BuyOrderService {
    private final BuyOrderRepository repository;
    private PortfolioService portfolioService;

    public BuyOrderService(BuyOrderRepository repository) {
        this.repository = repository;
    }

    public void setPortfolioService(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    public BuyOrder create(Ticker ticker, int quantity){
        BuyOrder order = new BuyOrder(ticker, quantity);
        repository.saveAndFlush(order);
        return order;
    }

    public boolean apply(long orderId, EOD dayData, long portfolioId) {
        BuyOrder order = repository.findById(orderId).orElse(null);//TODO order not existing test
        if(order==null) return false;
        if(isNotApplicable(order, dayData)) return false;

        int buyingPrice = dayData.getClose();
        int quantity = order.getQuantity();
        String symbol = dayData.getSymbol().getSymbol();

        boolean success = portfolioService.buy(portfolioId, symbol, buyingPrice, quantity);
        if (success) {
            order.setPending(false);
            repository.saveAndFlush(order);
            return true;
        } else {
            return false;
        }
    }

    private boolean isNotApplicable(Order order, EOD dayData) {
        return !order.isPending() || !dayData.getSymbol().getSymbol().equals(order.getTicker().getSymbol());
    }
}
