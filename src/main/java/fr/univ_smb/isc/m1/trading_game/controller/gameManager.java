package fr.univ_smb.isc.m1.trading_game.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class gameManager {
    @RequestMapping(value = { "/", "/index" })
    public String index() {
        return "gameManager";
    }
}
