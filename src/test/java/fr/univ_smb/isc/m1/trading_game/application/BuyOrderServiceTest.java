package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class BuyOrderServiceTest {

    private final static int mockPortfolioId = 0;
    private final static long mockOrderId = 0;

    Ticker mockTicker;
    EOD mockData;
    BuyOrder mockOrder;
    BuyOrderRepository mockRepository;
    PortfolioService mockPortfolioService;

    @BeforeEach
    public void setUp(){
        int quantity = 10;
        int closePrice = 500;

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
        when(mockOrder.getQuantity()).thenReturn(quantity);
        when(mockOrder.getTicker()).thenReturn(mockTicker);

        // Mock repository that always give the mock ticker
        mockRepository = mock(BuyOrderRepository.class);
        when(mockRepository.getOne(anyLong())).thenReturn(mockOrder);

        // Mock portfolio service that can always buy
        mockPortfolioService = mock(PortfolioService.class);
        when(mockPortfolioService.buy(anyInt(),any(),anyInt(),anyInt())).thenReturn(true);
    }

    @Test
    public void dontApplyWrongTicker(){
        // Mock unrelated ticker
        Ticker mockOtherTicker = mock(Ticker.class);
        when(mockOtherTicker.getSymbol()).thenReturn("WRONG");

        // Mock unrelated data
        EOD mockOtherEod = mock(EOD.class);
        when(mockOtherEod.getSymbol()).thenReturn(mockOtherTicker);

        // Service to test
        BuyOrderService service = new BuyOrderService(mockRepository, mockPortfolioService);

        Assertions.assertFalse(service.apply(mockOrderId, mockOtherEod, mockPortfolioId));
        verify(mockPortfolioService, never()).buy(anyLong(), any(), anyInt(), anyInt());
        verify(mockOrder, never()).setPending(false);
    }

    @Test
    public void applyCanAfford(){
       /* int quantity = 10;
        int close_price = 500;
        when(data.getClose()).thenReturn(close_price);
        when(ticker.getSymbol()).thenReturn("TEST");
        when(data.getSymbol()).thenReturn(ticker);
        when(portfolio.canAfford(anyInt())).thenReturn(true);
        BuyOrder buyOrder = new BuyOrder(portfolio, ticker, quantity);

        buyOrder.apply(data);

        verify(portfolio, times(1)).buy(data.getSymbol(), quantity, close_price*quantity);
        Assertions.assertFalse(buyOrder.isPending());*/
    }

    /*
    @Test
    public void applyAlreadyApplied(){
        int quantity = 10;
        int close_price = 500;
        when(data.getClose()).thenReturn(close_price);
        when(ticker.getSymbol()).thenReturn("TEST");
        when(data.getSymbol()).thenReturn(ticker);
        when(portfolio.canAfford(anyInt())).thenReturn(true);
        BuyOrder buyOrder = new BuyOrder(portfolio, ticker, quantity);
        buyOrder.setPending(false);

        buyOrder.apply(data);

        verify(portfolio, never()).buy(any(), anyInt(), anyInt());
        Assertions.assertFalse(buyOrder.isPending());
    }

    @Test
    public void applyCantAfford(){
        int quantity = 10;
        int close_price = 500;
        when(data.getClose()).thenReturn(close_price);
        when(ticker.getSymbol()).thenReturn("TEST");
        when(data.getSymbol()).thenReturn(ticker);
        when(portfolio.canAfford(anyInt())).thenReturn(false);
        BuyOrder buyOrder = new BuyOrder(portfolio, ticker, quantity);

        buyOrder.apply(data);

        verify(portfolio, never()).buy(any(), anyInt(), anyInt());
        Assertions.assertTrue(buyOrder.isPending());
    }*/
}
