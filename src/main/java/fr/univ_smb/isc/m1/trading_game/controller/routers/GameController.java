package fr.univ_smb.isc.m1.trading_game.controller.routers;

import fr.univ_smb.isc.m1.trading_game.application.*;
import fr.univ_smb.isc.m1.trading_game.controller.URLMap;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class GameController {
    private final GameService gameService;
    private final UserService userService;
    private final PlayerService playerService;
    private final TickerService tickerService;
    private final BuyOrderService buyOrderService;
    private final SellOrderService sellOrderService;
    private final PortfolioService portfolioService;

    public GameController(GameService gameService, UserService userService, PlayerService playerService, TickerService tickerService, BuyOrderService buyOrderService, SellOrderService sellOrderService, PortfolioService service) {
        this.gameService = gameService;
        this.userService = userService;
        this.playerService = playerService;
        this.tickerService = tickerService;
        this.buyOrderService = buyOrderService;
        this.sellOrderService = sellOrderService;
        portfolioService = service;
    }

    @GetMapping(value = URLMap.createOrder)
    public String createOrder(Model model,
                              @RequestParam(name = "gameId") long gameId,
                              @RequestParam(name = "playerId") long playerId,
                              @RequestParam(name = "portfolioId") long portfolioId){
        model.addAttribute("tickers", tickerService.getTickers());
        model.addAttribute("performCreateOrder", URLMap.performCreateOrder);
        model.addAttribute("playerId", playerId);
        model.addAttribute("portfolioId", portfolioId);
        model.addAttribute("gameId", gameId);
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
        redirectAttr.addFlashAttribute("portfolioNumber", portfolioNumber);
        return "redirect:"+URLMap.viewGame;
    }

    @GetMapping(value = URLMap.viewGame)
    public String gameManager(Model model,
                              @RequestParam(name="gameId") long gameId,
                              @RequestParam(name= "portfolioNumber", required = false) Optional<Integer> portfolioNumber) {
        Game g = gameService.getGame(gameId);
        TradingGameUser user = userService.getCurrentUser(SecurityContextHolder.getContext());
        Player player = gameService.getPlayer(gameId, user);
        if(g != null
                && user!=null
                && player != null){

            Portfolio currentPortfolio = playerService.getPortfolios(player.getId()).get(portfolioNumber.orElse(1)-1);
            List<TickerInfo> portfolioTickers = currentPortfolio.getParts()
                    .keySet()
                    .stream()
                    .map(t -> new TickerInfo(t, currentPortfolio.getQuantity(t)))
                    .collect(Collectors.toList());
            List<OrderInfo> portfolioOrders = currentPortfolio.getOrders()
                    .stream()
                    .map(OrderInfo::new)
                    .collect(Collectors.toList());

            model.addAttribute("totalBalance", playerService.getTotalBalance(player.getId())/100f);
            model.addAttribute("playerPortfolios", player.getPortfolios());
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

    private static class OrderInfo{
        private final String symbol;
        private final String type;
        private final String status;

        public OrderInfo(Order order) {
            this.symbol = order.getTicker().getSymbol();
            this.type = getType(order);
            this.status = order.isPending()?"en attente":"effectué";
        }

        public String getSymbol() {
            return symbol;
        }

        public String getType() {
            return type;
        }

        public String getStatus() {
            return status;
        }

        private String getType(Order o){
            if(o instanceof BuyOrder){
                return "Achat ("+o.getQuantity()+")";
            } else if (o instanceof SellOrder){
                return "Vente ("+o.getQuantity()+")";
            } else {
                return "Inconnu";
            }
        }
    }

    private static class TickerInfo{
        private final String symbol;
        private final int quantity;

        public TickerInfo(Ticker t, int quantity){
            this.symbol = t.getSymbol();
            this.quantity = quantity;
        }

        public String getSymbol() {
            return symbol;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
