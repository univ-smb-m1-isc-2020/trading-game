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
        when(mockOrder.isPending()).thenReturn(true);
        when(mockOrder.getQuantity()).thenReturn(quantity);
        when(mockOrder.getTicker()).thenReturn(mockTicker);

        // Mock repository that always give the mock ticker
        mockRepository = mock(BuyOrderRepository.class);
        when(mockRepository.getOne(anyLong())).thenReturn(mockOrder);

        // Mock portfolio service that can always buy
        mockPortfolioService = mock(PortfolioService.class);
        when(mockPortfolioService.buy(anyLong(),any(),anyInt(),anyInt())).thenReturn(true);
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
        verify(mockOrder, never()).setPending(anyBoolean());
        verify(mockRepository, never()).save(mockOrder);
    }

    @Test
    public void applyCanAfford(){
        // Service to test
        BuyOrderService service = new BuyOrderService(mockRepository, mockPortfolioService);

        Assertions.assertTrue(service.apply(mockOrderId, mockData, mockPortfolioId));

        verify(mockPortfolioService, times(1)).buy(mockPortfolioId, mockTicker.getSymbol(), mockData.getClose(), mockOrder.getQuantity());
        verify(mockOrder, times(1)).setPending(false);
        verify(mockOrder, never()).setPending(true);
        verify(mockRepository, atLeastOnce()).save(mockOrder);
    }


    @Test
    public void applyAlreadyApplied(){
        // Order has already been carried out
        when(mockOrder.isPending()).thenReturn(false);
        // Service to test
        BuyOrderService service = new BuyOrderService(mockRepository, mockPortfolioService);

        Assertions.assertFalse(service.apply(mockOrderId, mockData, mockPortfolioId));
        verify(mockPortfolioService, never()).buy(anyLong(), any(), anyInt(), anyInt());
        verify(mockOrder, never()).setPending(anyBoolean());
        verify(mockRepository, never()).save(mockOrder);
    }

    @Test
    public void applyCantAfford(){
        // Portfolio can't afford the desired quantity
        when(mockPortfolioService.buy(anyLong(),any(),anyInt(),anyInt())).thenReturn(false);
        // Service to test
        BuyOrderService service = new BuyOrderService(mockRepository, mockPortfolioService);

        Assertions.assertFalse(service.apply(mockOrderId, mockData, mockPortfolioId));
        verify(mockPortfolioService, times(1)).buy(anyLong(), any(), anyInt(), anyInt());
        verify(mockOrder, never()).setPending(anyBoolean());
        verify(mockRepository, never()).save(mockOrder);
    }
}
