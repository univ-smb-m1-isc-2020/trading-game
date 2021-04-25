package fr.univ_smb.isc.m1.trading_game.controller.routers;

import fr.univ_smb.isc.m1.trading_game.application.GameService;
import fr.univ_smb.isc.m1.trading_game.application.UserService;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TradingGameUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeAndGamePagesRouter {
    private final GameService gameService;

    public HomeAndGamePagesRouter(GameService gameService){
        this.gameService = gameService;
    }

    @RequestMapping(value = "/gameManager")
    public String gameManager() { return "gameManager"; }

    @RequestMapping(value = "/gameSelected")
    public String gameSelected() {
        return "gameSelected";
    }
}
