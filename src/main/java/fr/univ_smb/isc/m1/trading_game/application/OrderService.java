package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private PortfolioService portfolioService;

    public boolean apply(Order order, EOD dayData) {
        if (order instanceof BuyOrder) {
            return applyBuyOrder((BuyOrder) order, dayData);
        } else if (order instanceof SellOrder) {
            return applySellOrder((SellOrder) order, dayData);
        } else {
            return false;
        }
    }

    public boolean applyBuyOrder(BuyOrder order, EOD dayData) {
        if (isNotApplicable(order, dayData)) return false;

        int buyingPrice = dayData.getClose();
        int quantity = order.getQuantity();
        String symbol = dayData.getSymbol().getSymbol();
        long portId = order.getPortfolio().getId();

        boolean success = portfolioService.buy(portId, symbol, buyingPrice, quantity);

        if (success) {
            order.setPending(false);
            return true;
        } else {
            return false;
        }
    }

    public boolean applySellOrder(SellOrder order, EOD dayData) {
        if (isNotApplicable(order, dayData)) return false;

        int sellingPrice = dayData.getClose();
        int quantity = order.getQuantity();
        String symbol = dayData.getSymbol().getSymbol();
        long portId = order.getPortfolio().getId();

        boolean success = portfolioService.sell(portId, symbol, sellingPrice, quantity);

        if (success) {
            order.setPending(false);
            return true;
        } else {
            return false;
        }
    }

    private boolean isNotApplicable(Order order, EOD dayData) {
        return !order.isPending() || dayData.getSymbol() != order.getTicker();
    }

}
