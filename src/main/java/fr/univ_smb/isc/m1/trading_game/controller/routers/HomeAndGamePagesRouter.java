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

    private GameService gameService;
    private PlayerService playerService;

    public HomeAndGamePagesRouter(GameService gameService, PlayerService playerService){
        this.gameService = gameService;
        this.playerService = playerService;
        startStandardGame();
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

    private void startStandardGame() { // TEST
        String timezone = "Europe/Paris";
        int year = 2000;
        int month = Calendar.JANUARY;
        int date = 10;
        int hour = 0;
        int min = 0;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timezone));
        calendar.set(year,month,date,hour,min);
        Date startDate = calendar.getTime();
        Game game = gameService.createGame(3, 50, 5, startDate, 5);
        TradingGameUser p1 = new TradingGameUser("Tino Rossi", "12345");
        TradingGameUser p2 = new TradingGameUser("Agatha Christie", "12345");
        TradingGameUser p3 = new TradingGameUser("Lou Ferigno", "12345");
        gameService.addPlayer(game.getId(), p1);
        gameService.addPlayer(game.getId(), p2);
        gameService.addPlayer(game.getId(), p3);
        gameService.startGame(game.getId(), 10);
    }
}
