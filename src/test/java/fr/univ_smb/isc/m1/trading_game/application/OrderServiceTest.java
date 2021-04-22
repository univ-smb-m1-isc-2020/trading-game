package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class OrderServiceTest {
    private static final long mockPortfolioId = 0;
    private static final long mockOrderId = 0;
    private BuyOrderService buyOrderService;
    private SellOrderService sellOrderService;
    private EOD mockEod;
    private Ticker mockTicker;

    @BeforeEach
    public void setUp(){
        buyOrderService = mock(BuyOrderService.class);
        when(buyOrderService.apply(anyLong(),any(),anyLong())).thenReturn(true);
        sellOrderService = mock(SellOrderService.class);
        when(sellOrderService.apply(anyLong(),any(),anyLong())).thenReturn(true);

        mockTicker = mock(Ticker.class);
        when(mockTicker.getSymbol()).thenReturn("TEST");

        mockEod = mock(EOD.class);
        when(mockEod.getSymbol()).thenReturn(mockTicker);
    }

    @Test
    public void applyBuyOrder(){
        OrderService service = new OrderService(buyOrderService, sellOrderService);
        BuyOrder buyOrder = mock(BuyOrder.class);
        when(buyOrder.isPending()).thenReturn(true);
        when(buyOrder.getId()).thenReturn(mockOrderId);
        when(buyOrder.getTicker()).thenReturn(mockTicker);

        Assertions.assertTrue(service.apply(buyOrder, mockEod, mockPortfolioId));
        verify(buyOrderService, times(1)).apply(mockOrderId, mockEod, mockPortfolioId);
    }

    @Test
    public void applySellOrder(){
        OrderService service = new OrderService(buyOrderService, sellOrderService);
        SellOrder sellOrder = mock(SellOrder.class);
        when(sellOrder.isPending()).thenReturn(true);
        when(sellOrder.getId()).thenReturn(mockOrderId);
        when(sellOrder.getTicker()).thenReturn(mockTicker);

        Assertions.assertTrue(service.apply(sellOrder, mockEod, mockPortfolioId));
        verify(sellOrderService, times(1)).apply(mockOrderId, mockEod, mockPortfolioId);
    }

    @Test
    public void applyUnknownOrder(){
        OrderService service = new OrderService(buyOrderService, sellOrderService);
        Order order = mock(Order.class);
        when(order.isPending()).thenReturn(true);
        when(order.getId()).thenReturn(mockOrderId);
        when(order.getTicker()).thenReturn(mockTicker);

        Assertions.assertFalse(service.apply(order, mockEod, mockPortfolioId));
    }

    @Test
    public void dontApplyWrongTicker(){
        Ticker wrongTicker = mock(Ticker.class);
        when(wrongTicker.getSymbol()).thenReturn("WRONG");
        EOD wrongData = mock(EOD.class);
        when(wrongData.getSymbol()).thenReturn(wrongTicker);

        OrderService service = new OrderService(buyOrderService, sellOrderService);
        Order order = mock(Order.class);
        when(order.isPending()).thenReturn(true);
        when(order.getId()).thenReturn(mockOrderId);
        when(order.getTicker()).thenReturn(mockTicker);

        Assertions.assertFalse(service.apply(order, wrongData, mockPortfolioId));
    }

    @Test
    public void dontApplyAlreadyApplied(){
        OrderService service = new OrderService(buyOrderService, sellOrderService);
        Order order = mock(Order.class);
        when(order.isPending()).thenReturn(false);
        when(order.getId()).thenReturn(mockOrderId);
        when(order.getTicker()).thenReturn(mockTicker);

        Assertions.assertFalse(service.apply(order, mockEod, mockPortfolioId));
    }
}
