package fr.univ_smb.isc.m1.trading_game.controller.routers;

import fr.univ_smb.isc.m1.trading_game.application.UserService;
import fr.univ_smb.isc.m1.trading_game.controller.URLMap;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TradingGameUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = { "/", "/index" })
    public String index(Model model) {
        TradingGameUser user = userService.getCurrentUser(SecurityContextHolder.getContext());
        if(user==null){
            return "homePageCommon";
        } else {
            return "redirect:"+ URLMap.userHomepage;
        }
    }

    @GetMapping(value = URLMap.userHomepage)
    public String homePagePlayer(Model model) {
        TradingGameUser user = userService.getCurrentUser(SecurityContextHolder.getContext());
        model.addAttribute("admin",user.isAdmin());
        model.addAttribute("username",user.getUsername());
        return "homePageLogged";
    }

    @RequestMapping(value = URLMap.joinGame)
    public String joinGame() {
        return "joinGame";
    }

}
