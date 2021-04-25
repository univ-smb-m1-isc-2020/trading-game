package fr.univ_smb.isc.m1.trading_game.controller.routers;

import fr.univ_smb.isc.m1.trading_game.application.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationRouter {
    private final UserService userService;

    public AuthenticationRouter(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/logIn")
    public String logIn(Model model) {
        return "logIn";
    }

    @GetMapping(value = "/register")
    public String register(Model model) {
        return "register";
    }

    @PostMapping(value ="/performRegister")
    public String performRegister(@RequestParam(name = "username")String username,
                                  @RequestParam(name = "password")String password){
        userService.register(username, password);
        return "redirect:/homePagePlayer";
    }
}
