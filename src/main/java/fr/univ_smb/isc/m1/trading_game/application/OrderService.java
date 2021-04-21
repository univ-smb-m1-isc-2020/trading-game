package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.BuyOrder;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Order;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.SellOrder;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private BuyOrderService buyOrderService;
    private SellOrderService sellOrderService;

    public boolean apply(Order order, EOD dayData, long portfolioId) {
        if (isNotApplicable(order, dayData)) return false;
        if (order instanceof BuyOrder) {
            return buyOrderService.apply(order.getId(), dayData, portfolioId);
        } else if (order instanceof SellOrder) {
            return sellOrderService.apply((SellOrder) order, dayData, portfolioId);
        } else {
            return false;
        }
    }

    private boolean isNotApplicable(Order order, EOD dayData) {
        return !order.isPending() || dayData.getSymbol() != order.getTicker();
    }
}
