package fr.univ_smb.isc.m1.trading_game.controller.routers;

import fr.univ_smb.isc.m1.trading_game.controller.URLMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrderPagesRouter {
    @RequestMapping(value = URLMap.createOrder)
    public String createOrder(Model model) {
        return "createOrder";
    }
}
