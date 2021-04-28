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
    private final HeaderController headerController;
    private final GameService gameService;

    public AdminController(HeaderController headerController, GameService gameService) {
        this.headerController = headerController;
        this.gameService = gameService;
    }

    @GetMapping(value = URLMap.CREATE_GAME_PAGE)
    public String createGame(Model model) {
        headerController.loadHeaderParameters(model);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        model.addAttribute("dateFormat",format);
        model.addAttribute("performCreateGame",URLMap.PERFORM_CREATE_GAME);
        model.addAttribute("performStartGame",URLMap.PERFORM_START_GAME);
        model.addAttribute("startableGames", gameService.getUnstartedGames());
        return "createGame";
    }

    @PostMapping(value=URLMap.PERFORM_START_GAME)
    public String performCreateGame(@RequestParam(name = "duration")int duration,
                                    @RequestParam(name = "gameId") int gameId){
        int durationSeconds = duration * 60;
        gameService.startGame(gameId, durationSeconds);
        return "redirect:"+URLMap.CREATE_GAME_PAGE;
    }

    @PostMapping(value=URLMap.PERFORM_CREATE_GAME)
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
        return "redirect:"+URLMap.USER_HOMEPAGE;
    }
}
