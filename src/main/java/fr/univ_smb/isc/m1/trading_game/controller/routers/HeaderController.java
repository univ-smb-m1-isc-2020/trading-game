package fr.univ_smb.isc.m1.trading_game.controller.routers;

import fr.univ_smb.isc.m1.trading_game.application.UserService;
import fr.univ_smb.isc.m1.trading_game.controller.URLMap;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TradingGameUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class HeaderController {
    private final UserService userService;

    public HeaderController(UserService userService) {
        this.userService = userService;
    }

    public void loadHeaderParameters(Model model){
        TradingGameUser user = userService.getCurrentUser(SecurityContextHolder.getContext());
        model.addAttribute("performLogout", URLMap.performLogout);
        model.addAttribute("joinGame", URLMap.joinGame);
        model.addAttribute("createGamePage", URLMap.createGamePage);
        model.addAttribute("admin",user.isAdmin());
        model.addAttribute("username",user.getUsername());
    }
}
