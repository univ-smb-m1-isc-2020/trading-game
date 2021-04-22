package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PortfolioServiceTest {
    private final static long mockPortfolioId = 0;
    private final static String mockTickerMic = "TEST";

    private final static int closePrice = 5;
    private final static int quantity = 10;

    private Ticker mockTicker;
    private Portfolio mockPortfolio;
    private PortfolioRepository mockRepository;
    private OrderService mockOrderService;
    private TickerService mockTickerService;


    @BeforeEach
    public void setup(){
        // Mock ticker
        mockTicker = mock(Ticker.class);
        when(mockTicker.getSymbol()).thenReturn(mockTickerMic);

        // Mock portfolio
        mockPortfolio = mock(Portfolio.class);
        when(mockPortfolio.getId()).thenReturn(mockPortfolioId);
        when(mockPortfolio.getQuantity(mockTicker)).thenReturn(0);

        // Mock portfolio repository
        mockRepository = mock(PortfolioRepository.class);
        when(mockRepository.getOne(anyLong())).thenReturn(mockPortfolio);

        // Mock order service
        mockOrderService = mock(OrderService.class);

        //Mock ticker service
        mockTickerService = mock(TickerService.class);
        when(mockTickerService.get(any())).thenReturn(mockTicker);
    }

    @Test
    public void getOrders(){
        int orderCount = 3;
        List<Order> orders = new ArrayList<>();
        for(int i=0; i<orderCount; i++){
            orders.add(mock(Order.class));
        }
        when(mockPortfolio.getOrders()).thenReturn(orders);

        PortfolioService service = new PortfolioService(mockRepository, mockOrderService, mockTickerService);
        Assertions.assertEquals(orders, service.getOrders(mockPortfolioId));
    }

    @Test
    public void getParts(){
        int[] partCounts = new int[]{5,17,20};
        Map<Ticker, Integer> parts = new HashMap<>();
        for (int partCount : partCounts) {
            parts.put(mock(Ticker.class), partCount);
        }
        when(mockPortfolio.getParts()).thenReturn(parts);

        PortfolioService service = new PortfolioService(mockRepository, mockOrderService, mockTickerService);
        Assertions.assertEquals(parts, service.getParts(mockPortfolioId));
    }

    @Test
    public void applyOrders(){
        int orderCount = 3;

        EOD mockData = mock(EOD.class);
        List<Order> orders = new ArrayList<Order>();
        for(int i=0; i<orderCount; i++){
            orders.add(mock(Order.class));
        }

        when(mockPortfolio.getOrders()).thenReturn(orders);

        PortfolioService service = new PortfolioService(mockRepository, mockOrderService, mockTickerService);
        service.applyOrders(mockPortfolioId, mockData);

        for(int i=0; i<orderCount; i++){
            verify(mockOrderService, times(1)).apply(orders.get(i), mockData, mockPortfolioId);
        }
    }

    @Test
    public void buyCanAfford(){
        // Affordable case
        int cost = closePrice*quantity;
        int initialBalance = cost+1;
        when(mockPortfolio.getBalance()).thenReturn(initialBalance);

        PortfolioService service = new PortfolioService(mockRepository, mockOrderService, mockTickerService);
        Assertions.assertTrue(service.buy(mockPortfolioId, mockTickerMic, closePrice, quantity));
        verify(mockPortfolio, times(1)).setBalance(initialBalance-cost);
        verify(mockPortfolio, times(1)).setQuantity(mockTicker, quantity);
        verify(mockRepository, times(1)).save(mockPortfolio);
    }

    @Test
    public void buyCantAfford(){
        // Affordable case
        int cost = closePrice*quantity;
        int initialBalance = cost-1;
        when(mockPortfolio.getBalance()).thenReturn(initialBalance);

        PortfolioService service = new PortfolioService(mockRepository, mockOrderService, mockTickerService);
        Assertions.assertFalse(service.buy(mockPortfolioId, mockTickerMic, closePrice, quantity));
        verify(mockPortfolio, never()).setBalance(anyInt());
        verify(mockPortfolio, never()).setQuantity(any(), anyInt());
        verify(mockRepository, never()).save(mockPortfolio);
    }

    @Test
    public void canSell(){
        // Affordable case
        int initialBalance = 100;
        int sellQuantity = 10;
        int ownedQuantity = sellQuantity+1;
        when(mockPortfolio.getQuantity(mockTicker)).thenReturn(ownedQuantity);
        when(mockPortfolio.getBalance()).thenReturn(initialBalance);

        PortfolioService service = new PortfolioService(mockRepository, mockOrderService, mockTickerService);
        Assertions.assertTrue(service.sell(mockPortfolioId, mockTickerMic, closePrice, sellQuantity));
        verify(mockPortfolio, times(1)).setQuantity(mockTicker, ownedQuantity-sellQuantity);
        verify(mockPortfolio, times(1)).setBalance(initialBalance+closePrice*sellQuantity);
        verify(mockRepository, times(1)).save(mockPortfolio);
    }

    @Test
    public void cantSell(){
        // Affordable case
        int initialBalance = 100;
        int sellQuantity = 10;
        int ownedQuantity = sellQuantity-1;
        when(mockPortfolio.getQuantity(mockTicker)).thenReturn(ownedQuantity);
        when(mockPortfolio.getBalance()).thenReturn(initialBalance);

        PortfolioService service = new PortfolioService(mockRepository, mockOrderService, mockTickerService);
        Assertions.assertFalse(service.sell(mockPortfolioId, mockTickerMic, closePrice, sellQuantity));
        verify(mockPortfolio, never()).setQuantity(any(), anyInt());
        verify(mockPortfolio, never()).setBalance(anyInt());
        verify(mockRepository, never()).save(mockPortfolio);
    }
}
