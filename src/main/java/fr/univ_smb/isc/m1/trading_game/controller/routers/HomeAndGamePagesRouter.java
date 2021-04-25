package fr.univ_smb.isc.m1.trading_game.controller.routers;

import fr.univ_smb.isc.m1.trading_game.application.GameService;
import fr.univ_smb.isc.m1.trading_game.application.PlayerService;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Game;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Player;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TradingGameUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Controller
public class HomeAndGamePagesRouter {

    private final GameService gameService;
    private final PlayerService playerService;

    public HomeAndGamePagesRouter(GameService gameService, PlayerService playerService){
        this.gameService = gameService;
        this.playerService = playerService;
    }

    @RequestMapping(value = { "/", "/index" })
    public String index(Model model) {
        model.addAttribute("currentGames", gameService.getCurrentGames());
        return "homePageCommon";
    }

    @RequestMapping(value = "/homePagePlayer")
    public String homePagePlayer() {
        return "homePagePlayer";
    }

    @RequestMapping(value = "/homePageAdmin")
    public String homePageAdmin() {
        return "homePageAdmin";
    }

    @RequestMapping(value = "/createGame")
    public String createGame() {
        return "createGame";
    }

    @RequestMapping(value = "/joinGame")
    public String joinGame() {
        return "joinGame";
    }

    @RequestMapping(value = "/gameManager")
    public String gameManager() { return "gameManager"; }

    @RequestMapping(value = "/gameSelected")
    public String gameSelected() {
        return "gameSelected";
    }
}
