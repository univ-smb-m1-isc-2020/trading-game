package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Order;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.SellOrder;

public class SellOrderService {
    private PortfolioService portfolioService;

    public boolean apply(SellOrder order, EOD dayData, long portfolioId) {
        int sellingPrice = dayData.getClose();
        int quantity = order.getQuantity();
        String symbol = dayData.getSymbol().getSymbol();

        boolean success = portfolioService.sell(portfolioId, symbol, sellingPrice, quantity);

        if (success) {
            order.setPending(false);
            return true;
        } else {
            return false;
        }
    }
}
