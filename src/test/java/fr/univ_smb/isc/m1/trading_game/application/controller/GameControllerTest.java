package fr.univ_smb.isc.m1.trading_game.application.controller;

import fr.univ_smb.isc.m1.trading_game.application.*;
import fr.univ_smb.isc.m1.trading_game.controller.URLMap;
import fr.univ_smb.isc.m1.trading_game.controller.routers.GameController;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Player;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TradingGameUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class GameControllerTest {
    private GameService mockGameService;
    private UserService mockUserService;
    private PlayerService mockPlayerService;
    private TickerService mockTickerService;
    private BuyOrderService mockBuyOrderService;
    private SellOrderService mockSellOrderService;
    private PortfolioService mockPortfolioService;

    @BeforeEach
    public void setUp(){
        mockGameService = mock(GameService.class);
        mockUserService = mock(UserService.class);
        mockPlayerService = mock(PlayerService.class);
        mockTickerService = mock(TickerService.class);
        mockBuyOrderService = mock(BuyOrderService.class);
        mockSellOrderService = mock(SellOrderService.class);
        mockPortfolioService = mock(PortfolioService.class);
    }

    /*@Test
    public void performCreateOrder {
        GameController gameController = new GameController(
                mockGameService, mockUserService, mockPlayerService, mockTickerService,
                mockBuyOrderService, mockSellOrderService, mockPortfolioService
        );
        TradingGameUser mockUser = mock(TradingGameUser.class);
        Player mockPlayer = mock(Player.class);
        Ticker mockTicker = mock(Ticker.class);


    }*/
}
