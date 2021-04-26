package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.BuyOrder;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Order;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.SellOrder;
import org.springframework.stereotype.Service;

@Service
//Service used to operate orders without knowing their types
public class OrderService {
    private final BuyOrderService buyOrderService;
    private final SellOrderService sellOrderService;

    public OrderService(BuyOrderService buyOrderService, SellOrderService sellOrderService) {
        this.buyOrderService = buyOrderService;
        this.sellOrderService = sellOrderService;
    }

    public void setPortfolioService(PortfolioService service){
        buyOrderService.setPortfolioService(service);
        sellOrderService.setPortfolioService(service);
    }

    public boolean apply(Order order, EOD dayData, long portfolioId) {
        if (isNotApplicable(order, dayData)) return false;
        if (order instanceof BuyOrder) {
            return buyOrderService.apply(order.getId(), dayData, portfolioId);
        } else if (order instanceof SellOrder) {
            return sellOrderService.apply(order.getId(), dayData, portfolioId);
        } else {
            return false;
        }
    }

    private boolean isNotApplicable(Order order, EOD dayData) {
        return !order.isPending() || !dayData.getSymbol().getSymbol().equals(order.getTicker().getSymbol());
    }
}
