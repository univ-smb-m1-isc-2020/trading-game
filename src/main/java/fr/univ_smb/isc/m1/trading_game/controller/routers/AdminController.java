package fr.univ_smb.isc.m1.trading_game.controller.routers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

    private final static String adminPrefix ="/admin";

    @RequestMapping(value = adminPrefix +"/createGame")
    public String createGame() {
        return "createGame";
    }
}
