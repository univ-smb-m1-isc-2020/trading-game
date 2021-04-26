package fr.univ_smb.isc.m1.trading_game.controller.routers;

import fr.univ_smb.isc.m1.trading_game.application.GameService;
import fr.univ_smb.isc.m1.trading_game.controller.URLMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class AdminController {
    private final GameService gameService;

    public AdminController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping(value = URLMap.createGamePage)
    public String createGame(Model model) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        model.addAttribute("dateFormat",format);
        model.addAttribute("performCreateGame",URLMap.performCreateGame);
        model.addAttribute("performStartGame",URLMap.performStartGame);
        model.addAttribute("startableGames", gameService.getUnstartedGames());
        return "createGame";
    }

    @PostMapping(value=URLMap.performStartGame)
    public String performCreateGame(@RequestParam(name = "duration")int duration,
                                    @RequestParam(name = "gameId") int gameId){
        int durationSeconds = duration * 60;
        gameService.startGame(gameId, durationSeconds);
        return "redirect:"+URLMap.createGamePage;
    }

    @PostMapping(value=URLMap.performCreateGame)
    public String performCreateGame(@RequestParam(name = "duration")int duration,
                                    @RequestParam(name = "portfolioCount")int portfolioCount,
                                    @RequestParam(name = "initialBalance")int initialBalanceEuros,
                                    @RequestParam(name = "fee") int feeEuros,
                                    @RequestParam(name = "startDate") String startDateString){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = format.parse(startDateString);
            int initialBalance = initialBalanceEuros * 100;
            int fee = feeEuros * 100;
            gameService.createGame(portfolioCount, initialBalance, fee, startDate, duration);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "redirect:"+URLMap.userHomepage;
    }
}
