package fr.univ_smb.isc.m1.trading_game.controller.routers;

import fr.univ_smb.isc.m1.trading_game.application.*;
import fr.univ_smb.isc.m1.trading_game.controller.URLMap;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import fr.univ_smb.isc.m1.trading_game.view_objects.EODTickerInfo;
import fr.univ_smb.isc.m1.trading_game.view_objects.OrderInfo;
import fr.univ_smb.isc.m1.trading_game.view_objects.PortfolioTickerInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class GameController {
    private final HeaderController headerController;
    private final GameService gameService;
    private final UserService userService;
    private final PlayerService playerService;
    private final TickerService tickerService;
    private final EODService eodService;
    private final BuyOrderService buyOrderService;
    private final SellOrderService sellOrderService;
    private final PortfolioService portfolioService;

    public GameController(HeaderController headerController,
                          GameService gameService,
                          UserService userService,
                          PlayerService playerService,
                          TickerService tickerService,
                          EODService eodService,
                          BuyOrderService buyOrderService,
                          SellOrderService sellOrderService,
                          PortfolioService service) {
        this.headerController = headerController;
        this.gameService = gameService;
        this.userService = userService;
        this.playerService = playerService;
        this.tickerService = tickerService;
        this.eodService = eodService;
        this.buyOrderService = buyOrderService;
        this.sellOrderService = sellOrderService;
        portfolioService = service;
    }

    @GetMapping(value = URLMap.createOrder)
    public String createOrder(Model model,
                              @RequestParam(name = "gameId") long gameId,
                              @RequestParam(name = "playerId") long playerId,
                              @RequestParam(name = "portfolioId") long portfolioId){
        headerController.loadHeaderParameters(model);
        model.addAttribute("tickers", tickerService.getTickers()
                .stream()
                .map(t -> {
                    EOD last = eodService.getLast(t);
                    if(last!=null){
                        return new EODTickerInfo(t, last.getClose());
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );
        model.addAttribute("performCreateOrder", URLMap.performCreateOrder);
        model.addAttribute("playerId", playerId);
        model.addAttribute("portfolioId", portfolioId);
        model.addAttribute("portfolioBalance", portfolioService.getBalance(portfolioId)/100.0);
        model.addAttribute("gameId", gameId);

        String cancelUrl = UriComponentsBuilder.fromUriString(URLMap.viewGame)
                .queryParam("gameId",gameId)
                .queryParam("portfolioNumber",playerService.getPortfolioNumber(playerId, portfolioId)).toUriString();
        model.addAttribute("gameManagement", cancelUrl);
        return "createOrder";
    }

    @PostMapping(value = URLMap.performCreateOrder)
    public String performCreateOrder(RedirectAttributes redirectAttr,
                                     @RequestParam(name="orderType") String type,
                                     @RequestParam(name="orderTicker") String tickerMic,
                                     @RequestParam(name="orderQuantity") int quantity,
                                     @RequestParam(name="playerId") long playerId,
                                     @RequestParam(name="portfolioId") long portfolioId,
                                     @RequestParam(name="gameId") long gameId){
        TradingGameUser user = userService.getCurrentUser(SecurityContextHolder.getContext());
        Player player = playerService.getPlayer(playerId);
        Ticker ticker = tickerService.get(tickerMic);
        int portfolioNumber = playerService.getPortfolioNumber(playerId, portfolioId);
        if (user != null && ticker != null && player.getId() == playerId && player.getPortfolios().stream().anyMatch(p -> p.getId() == portfolioId)) {
            if(type.equals("buy")){
                BuyOrder order = buyOrderService.create(ticker, quantity);
                portfolioService.addOrder(portfolioId, order);
            } else if(type.equals("sell")){
                SellOrder order = sellOrderService.create(ticker, quantity);
                portfolioService.addOrder(portfolioId, order);
            }
        }

        redirectAttr.addAttribute("gameId",gameId);
        redirectAttr.addAttribute("portfolioNumber", portfolioNumber);
        return "redirect:"+URLMap.viewGame;
    }

    @GetMapping(value = URLMap.viewGame)
    public String gameManager(Model model,
                              @RequestParam(name="gameId") long gameId,
                              @RequestParam(name= "portfolioNumber", required = false) Optional<Integer> portfolioNumber) {
        headerController.loadHeaderParameters(model);
        Game g = gameService.getGame(gameId);
        TradingGameUser user = userService.getCurrentUser(SecurityContextHolder.getContext());
        Player player = gameService.getPlayer(gameId, user);
        if(g != null
                && user!=null
                && player != null){

            Portfolio currentPortfolio = playerService.getPortfolios(player.getId()).get(portfolioNumber.orElse(1)-1);
            List<PortfolioTickerInfo> portfolioTickers = getTickerInfo(currentPortfolio);
            List<OrderInfo> portfolioOrders = getOrderInfo(currentPortfolio);

            model.addAttribute("totalBalance", playerService.getTotalBalance(player.getId())/100f);
            model.addAttribute("playerPortfolios", playerService.getPortfolios(player.getId()));
            if(portfolioNumber.isPresent()){
                model.addAttribute("currentPortfolioNumber", portfolioNumber.get());
            } else {
                model.addAttribute("currentPortfolioNumber", 1);
            }
            model.addAttribute("portfolioTickers", portfolioTickers);
            model.addAttribute("currentPortfolioBalance", currentPortfolio.getBalance()/100f);
            model.addAttribute("portfolioOrders", portfolioOrders);
            model.addAttribute("currentPortfolioId", currentPortfolio.getId());
            model.addAttribute("playerId", player.getId());
            model.addAttribute("gameId", gameId);

            model.addAttribute("viewPortfolio", URLMap.viewGame+"?gameId="+gameId+"&portfolioNumber=");
            model.addAttribute("createOrder",URLMap.createOrder);

            return "gameManager";
        } else {
            return "redirect:"+URLMap.userHomepage;
        }
    }

    private List<OrderInfo> getOrderInfo(Portfolio currentPortfolio){
        return currentPortfolio.getOrders()
                .stream()
                .sorted(Comparator.comparingLong(Order::getId))
                .map(OrderInfo::new)
                .collect(Collectors.toList());
    }

    private List<PortfolioTickerInfo> getTickerInfo(Portfolio currentPortfolio){
        return currentPortfolio.getParts()
                .keySet()
                .stream()
                .map(t -> {
                    Ticker ticker = tickerService.get(t);
                    EOD last = eodService.getLast(ticker);
                    if(last!=null){
                        return new PortfolioTickerInfo(ticker,
                                currentPortfolio.getQuantity(t),
                                eodService.getLast(ticker).getClose());
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
