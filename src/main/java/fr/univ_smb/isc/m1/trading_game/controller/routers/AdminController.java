package fr.univ_smb.isc.m1.trading_game.controller.routers;

import fr.univ_smb.isc.m1.trading_game.application.GameService;
import fr.univ_smb.isc.m1.trading_game.controller.URLMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class AdminController {
    private final GameService gameService;

    public AdminController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping(value = URLMap.createGamePage)
    public String createGame(Model model) {
        model.addAttribute("performCreateGame",URLMap.performCreateGame);
        return "createGame";
    }

    @PostMapping(value=URLMap.performCreateGame)
    public String performCreateGame(@RequestParam(name = "duration")int duration,
                                    @RequestParam(name = "portfolioCount")int portfolioCount,
                                    @RequestParam(name = "initialBalance")int initialBalanceDollars,
                                    @RequestParam(name = "fee") int feeDollars,
                                    @RequestParam(name = "startDate") Date startDate){
        int initialBalance = initialBalanceDollars*100;
        int fee = feeDollars * 100;
        gameService.createGame(portfolioCount, initialBalance, fee, startDate, duration);
        return "redirect:"+URLMap.userHomepage;
    }
}
