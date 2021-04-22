package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

public class SellOrderServiceTest {

    private final static long mockPortfolioId = 0;
    private final static long mockOrderId = 0;

    private Ticker mockTicker;
    private EOD mockData;
    private SellOrder mockOrder;
    private SellOrderRepository mockRepository;
    private PortfolioService mockPortfolioService;

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
        mockOrder = mock(SellOrder.class);
        when(mockOrder.getId()).thenReturn(mockOrderId);
        when(mockOrder.isPending()).thenReturn(true);
        when(mockOrder.getQuantity()).thenReturn(quantity);
        when(mockOrder.getTicker()).thenReturn(mockTicker);

        // Mock repository that always give the mock ticker
        mockRepository = mock(SellOrderRepository.class);
        when(mockRepository.findById(anyLong())).thenReturn(Optional.of(mockOrder));

        // Mock portfolio service that can always buy
        mockPortfolioService = mock(PortfolioService.class);
        when(mockPortfolioService.sell(anyLong(),any(),anyInt(),anyInt())).thenReturn(true);
    }

    @Test
    public void createSellOrder(){
        // Service to test
        Ticker ticker = mock(Ticker.class);
        int quantity = 5;
        SellOrderService service = new SellOrderService(mockRepository, mockPortfolioService);
        SellOrder order = service.create(ticker, quantity);
        verify(mockRepository,times(1)).save(order);
        Assertions.assertEquals(ticker, order.getTicker());
        Assertions.assertEquals(quantity, order.getQuantity());
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
        SellOrderService service = new SellOrderService(mockRepository, mockPortfolioService);

        Assertions.assertFalse(service.apply(mockOrderId, mockOtherEod, mockPortfolioId));
        verify(mockPortfolioService, never()).sell(anyLong(), any(), anyInt(), anyInt());
        verify(mockOrder, never()).setPending(anyBoolean());
        verify(mockRepository, never()).save(mockOrder);
    }

    @Test
    public void applyCanSell(){
        // Service to test
        SellOrderService service = new SellOrderService(mockRepository, mockPortfolioService);

        Assertions.assertTrue(service.apply(mockOrderId, mockData, mockPortfolioId));

        verify(mockPortfolioService, times(1)).sell(mockPortfolioId, mockTicker.getSymbol(), mockData.getClose(), mockOrder.getQuantity());
        verify(mockOrder, times(1)).setPending(false);
        verify(mockOrder, never()).setPending(true);
        verify(mockRepository, atLeastOnce()).save(mockOrder);
    }

    @Test
    public void applyAlreadyApplied(){
        // Order has already been carried out
        when(mockOrder.isPending()).thenReturn(false);
        // Service to test
        SellOrderService service = new SellOrderService(mockRepository, mockPortfolioService);

        Assertions.assertFalse(service.apply(mockOrderId, mockData, mockPortfolioId));
        verify(mockPortfolioService, never()).sell(anyLong(), any(), anyInt(), anyInt());
        verify(mockOrder, never()).setPending(anyBoolean());
        verify(mockRepository, never()).save(mockOrder);
    }

    @Test
    public void applyCantSell(){
        // Portfolio can't afford the desired quantity
        when(mockPortfolioService.sell(anyLong(),any(),anyInt(),anyInt())).thenReturn(false);
        // Service to test
        SellOrderService service = new SellOrderService(mockRepository, mockPortfolioService);

        Assertions.assertFalse(service.apply(mockOrderId, mockData, mockPortfolioId));
        verify(mockPortfolioService, times(1)).sell(anyLong(), any(), anyInt(), anyInt());
        verify(mockOrder, never()).setPending(anyBoolean());
        verify(mockRepository, never()).save(mockOrder);
    }
}
