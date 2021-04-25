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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class UserController {
    private final GameService gameService;
    private final UserService userService;

    public UserController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping(value = { "/", "/index" })
    public String index(Model model) {
        TradingGameUser user = userService.getCurrentUser(SecurityContextHolder.getContext());
        if(user==null){
            model.addAttribute("loginPage", URLMap.loginPage);

            List<Game> onGoingGames = gameService.getCurrentGames();
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            model.addAttribute("dateFormat", format);
            model.addAttribute("currentGames", onGoingGames);
            return "homePageCommon";
        } else {
            return "redirect:"+ URLMap.userHomepage;
        }
    }

    @GetMapping(value = URLMap.userHomepage)
    public String homePagePlayer(Model model) {
        TradingGameUser user = userService.getCurrentUser(SecurityContextHolder.getContext());
        List<Game> onGoingGames = gameService.getGamesOf(user);

        model.addAttribute("performLogout", URLMap.performLogout);
        model.addAttribute("joinGame", URLMap.joinGame);
        model.addAttribute("createGamePage", URLMap.createGamePage);

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        model.addAttribute("dateFormat", format);
        model.addAttribute("joinedGames", onGoingGames);
        model.addAttribute("admin",user.isAdmin());
        model.addAttribute("username",user.getUsername());
        return "homePageLogged";
    }

    @GetMapping(value = URLMap.joinGame)
    public String joinGame(Model model) {
        TradingGameUser user = userService.getCurrentUser(SecurityContextHolder.getContext());
        List<Game> availableGames = gameService.getAvailableGames(user);

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        model.addAttribute("dateFormat", format);
        model.addAttribute("performJoinGame", URLMap.performJoinGame);
        model.addAttribute("availableGames",availableGames);
        return "joinGame";
    }

    @PostMapping(value = URLMap.performJoinGame)
    public String performJoinGame(@RequestParam(name = "gameId") long gameId){
        TradingGameUser user = userService.getCurrentUser(SecurityContextHolder.getContext());
        gameService.addPlayer(gameId, user);
        //TODO redirect to game page
        return "redirect:"+URLMap.userHomepage;
    }

}
