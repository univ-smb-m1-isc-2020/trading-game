package fr.univ_smb.isc.m1.trading_game.view_objects;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.BuyOrder;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Order;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.SellOrder;

public class OrderInfo{
    private final String symbol;
    private final String type;
    private final String status;

    public OrderInfo(Order order) {
        this.symbol = order.getTicker().getSymbol();
        this.type = getType(order);
        this.status = order.isPending()?"en attente":"effectué";
    }

    public String getSymbol() {
        return symbol;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    private String getType(Order o){
        if(o instanceof BuyOrder){
            return "Achat ("+o.getQuantity()+")";
        } else if (o instanceof SellOrder){
            return "Vente ("+o.getQuantity()+")";
        } else {
            return "Inconnu";
        }
    }
}