package fr.univ_smb.isc.m1.trading_game.controller.routers;

import fr.univ_smb.isc.m1.trading_game.controller.URLMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    @GetMapping(value = URLMap.createGamePage)
    public String createGame(Model model) {
        model.addAttribute("performCreateGame",URLMap.performCreateGame);
        return "createGame";
    }

    @PostMapping(value=URLMap.performCreateGame)
    public String performCreateGame(@RequestParam(name = "username")String username,
                                    @RequestParam(name = "password")String password){
        //TODO
        return "redirect:"+URLMap.userHomepage;
    }
}
