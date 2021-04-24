package fr.univ_smb.isc.m1.trading_game.controller;

import fr.univ_smb.isc.m1.trading_game.application.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Router {
    private final UserService userService;

    public Router(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = { "/", "/index" })
    public String index() {
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

    @RequestMapping(value = "/gameManager")
    public String gameManager() { return "gameManager"; }

    @RequestMapping(value = "/createOrder")
    public String createOrder() {
        return "createOrder";
    }

    @RequestMapping(value = "/createGame")
    public String createGame() {
        return "createGame";
    }

    @RequestMapping(value = "/joinGame")
    public String joinGame() {
        return "joinGame";
    }

    @RequestMapping(value = "/gameSelected")
    public String gameSelected() {
        return "gameSelected";
    }

    @GetMapping(value = "/logIn")
    public String logIn() {
        return "logIn";
    }

    @GetMapping(value = "/register")
    public String register() {
        return "register";
    }

    @PostMapping(value ="/performRegister")
    public String performRegister(@RequestParam(name = "username")String username,
                                  @RequestParam(name = "password")String password){
        userService.register(username, password);
        return "redirect:/homePagePlayer";
    }
}