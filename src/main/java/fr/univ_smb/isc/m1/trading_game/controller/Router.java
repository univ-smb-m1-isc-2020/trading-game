package fr.univ_smb.isc.m1.trading_game.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Router {
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
}