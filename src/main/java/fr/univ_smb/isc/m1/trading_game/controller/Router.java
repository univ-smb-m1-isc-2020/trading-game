package fr.univ_smb.isc.m1.trading_game.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Router {
    @RequestMapping(value = { "/", "/index" })
    public String index() {
        return "homePagePlayer";
    }

    @RequestMapping(value = "/gameManager")
    public String gameManager(Model model) {
        //model.addAttribute("currentWallet", player.currentWallet.getBalance()); // TODO : faire le lien avec le modèle
        return "gameManager";
    }

    @RequestMapping(value = "/createOrder")
    public String createOrder() {
        return "createOrder";
    }

    @RequestMapping(value = "/joinGame")
    public String joinGame() {
        return "joinGame";
    }
}