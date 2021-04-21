package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Order;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Player;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private OrderService orderService;

    public void applyOrders(long playerId, EOD dayData){
        //TODO
    }
}
