package fr.univ_smb.isc.m1.trading_game.controller;

import fr.univ_smb.isc.m1.trading_game.application.*;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Game;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Order;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Player;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sound.sampled.Port;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Controller
public class Router {
    /* Chaque service doit constituer un attribut */
    private BuyOrderService bos;
    private SellOrderService sos;
    private OrderService os;
    private EODService eods;
    private GameService gs;
    private PlayerService ps;
    private PortfolioService pfs;
    private TickerService ts;
    private HistoricalDataService hds;

    public Router(BuyOrderService bos, SellOrderService sos, OrderService os,
                  EODService eods, GameService gs, PlayerService ps, PortfolioService pfs,
                  TickerService ts, HistoricalDataService hds){
        this.bos = bos;
        this.sos = sos;
        this.os = os;
        this.eods = eods;
        this.gs = gs;
        this.ps = ps;
        this.pfs = pfs;
        this.ts = ts;
        this.hds = hds;

        /* TEST */
        String timezone = "Europe/Paris";
        int year = 2000;
        int month = Calendar.JANUARY;
        int date = 10;
        int hour = 0;
        int min = 0;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timezone));
        calendar.set(year,month,date,hour,min);
        Game test = gs.createGame(3, 50, 2, calendar.getTime(), 5);
        Player p1 = new Player();
        gs.startGame(test.getId(), 5);
    }

    @RequestMapping(value = { "/", "/index" })
    public String index(Model model) {
        model.addAttribute("games", gs.getGames());
        return "homePageCommon";
    }

    @RequestMapping(value = "/homePagePlayer")
    public String homePagePlayer(Model model) {
        return "homePagePlayer";
    }

    @RequestMapping(value = "/homePageAdmin")
    public String homePageAdmin(Model model) {
        return "homePageAdmin";
    }

    @RequestMapping(value = "/gameManager")
    public String gameManager(Model model) { return "gameManager"; }

    @RequestMapping(value = "/createOrder")
    public String createOrder(Model model) {
        model.addAttribute("tickers", ts.getTickers());
        return "createOrder";
    }

    @RequestMapping(value = "/createGame")
    public String createGame(Model model) {
        return "createGame";
    }

    @RequestMapping(value = "/joinGame")
    public String joinGame(Model model) {
        return "joinGame";
    }

    @RequestMapping(value = "/gameSelected")
    public String gameSelected(Model model) {
        return "gameSelected";
    }

    @RequestMapping(value = "/logIn")
    public String logIn(Model model) {
        return "logIn";
    }

    @RequestMapping(value = "/register")
    public String register(Model model) {
        return "register";
    }
}