package fr.univ_smb.isc.m1.trading_game.controller.routers;

import fr.univ_smb.isc.m1.trading_game.application.GameService;
import fr.univ_smb.isc.m1.trading_game.application.UserService;
import fr.univ_smb.isc.m1.trading_game.controller.URLMap;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Game;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TradingGameUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class UserController {
    private final static String DATE_FORMAT = "dd/MM/yyyy";
    private final HeaderController headerController;
    private final GameService gameService;
    private final UserService userService;

    public UserController(HeaderController headerController, GameService gameService, UserService userService) {
        this.headerController = headerController;
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping(value = { "/", "/index" })
    public String index(Model model) {
        TradingGameUser user = userService.getCurrentUser(SecurityContextHolder.getContext());
        if(user==null){
            model.addAttribute("loginPage", URLMap.LOGIN_PAGE);

            List<Game> onGoingGames = gameService.getCurrentlyActiveGames();
            DateFormat format = new SimpleDateFormat(DATE_FORMAT);
            model.addAttribute("dateFormat", format);
            model.addAttribute("currentGames", onGoingGames);
            return "homePageCommon";
        } else {
            return "redirect:"+ URLMap.USER_HOMEPAGE;
        }
    }

    @GetMapping(value = URLMap.USER_HOMEPAGE)
    public String homePagePlayer(Model model) {
        headerController.loadHeaderParameters(model);
        TradingGameUser user = userService.getCurrentUser(SecurityContextHolder.getContext());
        List<Game> onGoingGames = gameService.getActiveGamesOf(user);
        List<Game> endedGames = gameService.getEndedGamesOf(user);
        model.addAttribute("viewGame", URLMap.VIEW_GAME);
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        model.addAttribute("dateFormat", format);
        model.addAttribute("joinedGames", onGoingGames);
        model.addAttribute("endedGames", endedGames);
        return "homePageLogged";
    }

    @GetMapping(value = URLMap.JOIN_GAME)
    public String joinGame(Model model) {
        headerController.loadHeaderParameters(model);
        TradingGameUser user = userService.getCurrentUser(SecurityContextHolder.getContext());
        List<Game> availableGames = gameService.getAvailableGames(user);

        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        model.addAttribute("dateFormat", format);
        model.addAttribute("performJoinGame", URLMap.PERFORM_JOIN_GAME);
        model.addAttribute("availableGames",availableGames);
        return "joinGame";
    }

    @PostMapping(value = URLMap.PERFORM_JOIN_GAME)
    public String performJoinGame(@RequestParam(name = "gameId") long gameId,
                                  RedirectAttributes redirectAttrs){
        TradingGameUser user = userService.getCurrentUser(SecurityContextHolder.getContext());
        gameService.addPlayer(gameId, user);
        redirectAttrs.addAttribute("gameId",gameId);
        return "redirect:"+URLMap.VIEW_GAME;
    }


}
