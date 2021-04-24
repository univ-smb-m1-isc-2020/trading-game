package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.springframework.stereotype.Service;

@Service
public class SellOrderService {
    private final SellOrderRepository repository;
    private PortfolioService portfolioService;

    public SellOrderService(SellOrderRepository repository) {
        this.repository = repository;
    }

    public void setPortfolioService(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    public SellOrder create(Ticker ticker, int quantity){
        SellOrder order = new SellOrder(ticker, quantity);
        repository.saveAndFlush(order);
        return order;
    }

    public boolean apply(long orderId, EOD dayData, long portfolioId) {
        SellOrder order = repository.findById(orderId).orElse(null);
        if(order==null) return false;//TODO test
        if(isNotApplicable(order,dayData)) return false;

        int sellingPrice = dayData.getClose();
        int quantity = order.getQuantity();
        String symbol = dayData.getSymbol().getSymbol();

        boolean success = portfolioService.sell(portfolioId, symbol, sellingPrice, quantity);

        if (success) {
            order.setPending(false);
            repository.saveAndFlush(order);
            return true;
        } else {
            return false;
        }
    }

    private boolean isNotApplicable(Order order, EOD dayData) {
        return !order.isPending() || dayData.getSymbol() != order.getTicker();
    }
}
