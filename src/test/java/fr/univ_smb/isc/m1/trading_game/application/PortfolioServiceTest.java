package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PortfolioServiceTest {
    private final static long mockPortfolioId = 0;
    private final static long mockOrderId = 0;

    private final static int baseValue = 500;
    private final static int closePrice = 5;
    private final static int quantity = 10;

    private Ticker mockTicker;
    private EOD mockData;
    private BuyOrder mockOrder;
    private Portfolio mockPortfolio;
    private PortfolioRepository repository;
    private OrderService orderService;
    private TickerService tickerService;


    @BeforeEach
    public void setup(){
        // Mock ticker
        mockTicker = mock(Ticker.class);
        when(mockTicker.getSymbol()).thenReturn("TEST");

        // Mock EOD
        mockData = mock(EOD.class);
        when(mockData.getClose()).thenReturn(closePrice);
        when(mockData.getSymbol()).thenReturn(mockTicker);

        // Mock order, about the mock ticker
        mockOrder = mock(BuyOrder.class);
        when(mockOrder.getId()).thenReturn(mockOrderId);
        when(mockOrder.isPending()).thenReturn(true);
        when(mockOrder.getQuantity()).thenReturn(quantity);
        when(mockOrder.getTicker()).thenReturn(mockTicker);

        // Mock portfolio
        mockPortfolio = mock(Portfolio.class);
        when(mockPortfolio.getId()).thenReturn(mockPortfolioId);

        // Mock portfolio repository
        repository = mock(PortfolioRepository.class);
        when(repository.getOne(anyLong())).thenReturn(mockPortfolio);

        // Mock order service
        orderService = mock(OrderService.class);

        //Mock ticker service
        tickerService = mock(TickerService.class);
    }

    @Test
    public void applyOrders(){

    }

    @Test
    public void buy(){
        /*Ticker ticker = mock(Ticker.class);
        int quantity = 10;
        int unitValue = 5;
        portfolio.buy(ticker, quantity, quantity*unitValue);
        Assertions.assertEquals(baseValue-quantity*unitValue, portfolio.getBalance());*/
    }

    @Test
    public void sell(){
        /*Ticker ticker = mock(Ticker.class);
        int quantity = 10;
        int unitValue = 5;
        portfolio.setQuantity(ticker, quantity);
        portfolio.sell(ticker, quantity, quantity*unitValue);
        Assertions.assertEquals(baseValue+quantity*unitValue,portfolio.getBalance());*/
    }
}
