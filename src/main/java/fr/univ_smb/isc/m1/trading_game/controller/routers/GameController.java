package fr.univ_smb.isc.m1.trading_game.controller.routers;

import fr.univ_smb.isc.m1.trading_game.application.GameService;
import fr.univ_smb.isc.m1.trading_game.application.PlayerService;
import fr.univ_smb.isc.m1.trading_game.application.TickerService;
import fr.univ_smb.isc.m1.trading_game.application.UserService;
import fr.univ_smb.isc.m1.trading_game.controller.URLMap;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class GameController {
    private final GameService gameService;
    private final UserService userService;
    private final PlayerService playerService;
    private final TickerService tickerService;

    public GameController(GameService gameService, UserService userService, PlayerService playerService, TickerService tickerService) {
        this.gameService = gameService;
        this.userService = userService;
        this.playerService = playerService;
        this.tickerService = tickerService;
    }

    @GetMapping(value = URLMap.createOrder)
    public String createOrder(Model model,
                              @RequestParam(name = "playerId") long playerId,
                              @RequestParam(name = "portfolioId") long portfolioId){
        model.addAttribute("tickers", tickerService.getTickers());
        model.addAttribute("performCreateOrder", URLMap.performCreateOrder);
        model.addAttribute("playerId", playerId);
        model.addAttribute("portfolioId", portfolioId);
        return "createOrder";
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

            Portfolio currentPortfolio = player.getPortfolios().get(portfolioNumber.orElse(1)-1);
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
