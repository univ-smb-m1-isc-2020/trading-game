package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Order;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.SellOrder;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.SellOrderRepository;
import org.springframework.stereotype.Service;

@Service
public class SellOrderService {
    private final SellOrderRepository repository;
    private final PortfolioService portfolioService;

    public SellOrderService(SellOrderRepository repository, PortfolioService portfolioService) {
        this.repository = repository;
        this.portfolioService = portfolioService;
    }

    public boolean apply(long orderId, EOD dayData, long portfolioId) {
        SellOrder order = repository.getOne(orderId);
        int sellingPrice = dayData.getClose();
        int quantity = order.getQuantity();
        String symbol = dayData.getSymbol().getSymbol();

        boolean success = portfolioService.sell(portfolioId, symbol, sellingPrice, quantity);

        if (success) {
            order.setPending(false);
            repository.save(order);
            return true;
        } else {
            return false;
        }
    }
}
