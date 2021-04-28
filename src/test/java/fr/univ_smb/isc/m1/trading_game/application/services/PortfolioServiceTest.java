package fr.univ_smb.isc.m1.trading_game.application.services;

import fr.univ_smb.isc.m1.trading_game.application.OrderService;
import fr.univ_smb.isc.m1.trading_game.application.PortfolioService;
import fr.univ_smb.isc.m1.trading_game.application.TickerService;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

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
        when(mockPortfolio.getQuantity(mockTicker.getSymbol())).thenReturn(0);

        // Mock portfolio repository
        mockRepository = mock(PortfolioRepository.class);
        when(mockRepository.findById(anyLong())).thenReturn(Optional.of(mockPortfolio));

        // Mock order service
        mockOrderService = mock(OrderService.class);

        //Mock ticker service
        mockTickerService = mock(TickerService.class);
        when(mockTickerService.get(any())).thenReturn(mockTicker);
    }

    @Test
    public void getOrders(){
        int orderCount = 3;
        Set<Order> orders = new HashSet<>();
        for(int i=0; i<orderCount; i++){
            orders.add(mock(Order.class));
        }
        when(mockPortfolio.getOrders()).thenReturn(orders);

        PortfolioService service = new PortfolioService(mockRepository, mockOrderService, mockTickerService);
        Assertions.assertEquals(orders, new HashSet<>(service.getOrders(mockPortfolioId)));
    }

    @Test
    public void addOrder(){
        Set<Order> orders = new HashSet<>();
        Order mockOrder = mock(Order.class);
        when(mockRepository.findById(mockPortfolioId)).thenReturn(Optional.of(mockPortfolio));
        when(mockPortfolio.getOrders()).thenReturn(orders);

        PortfolioService service = new PortfolioService(mockRepository, mockOrderService, mockTickerService);
        service.addOrder(mockPortfolioId, mockOrder);
        Assertions.assertTrue(orders.contains(mockOrder));
        verify(mockRepository, times(1)).saveAndFlush(mockPortfolio);
    }

    @Test
    public void addNullOrder(){
        when(mockRepository.findById(mockPortfolioId)).thenReturn(Optional.of(mockPortfolio));
        PortfolioService service = new PortfolioService(mockRepository, mockOrderService, mockTickerService);
        service.addOrder(mockPortfolioId, null);
        verify(mockRepository, never()).saveAndFlush(any());
    }

    @Test
    public void addOrderNullPortfolio(){
        long nullId = 1;
        when(mockRepository.findById(nullId)).thenReturn(Optional.empty());
        PortfolioService service = new PortfolioService(mockRepository, mockOrderService, mockTickerService);
        service.addOrder(nullId, mock(Order.class));
        verify(mockRepository, never()).saveAndFlush(any());
    }

    @Test
    public void applyOrders(){
        int orderCount = 3;

        EOD mockData = mock(EOD.class);
        Set<Order> orders = new HashSet<>();
        for(int i=0; i<orderCount; i++){
            orders.add(mock(Order.class));
        }

        when(mockPortfolio.getOrders()).thenReturn(orders);

        PortfolioService service = new PortfolioService(mockRepository, mockOrderService, mockTickerService);
        service.applyOrders(mockPortfolioId, mockData);

        for(Order o : orders){
            verify(mockOrderService, times(1)).apply(o, mockData, mockPortfolioId);
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
        verify(mockRepository, times(1)).saveAndFlush(mockPortfolio);
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
        verify(mockRepository, never()).saveAndFlush(mockPortfolio);
    }

    @Test
    public void canSell(){
        // Affordable case
        int initialBalance = 100;
        int sellQuantity = 10;
        int ownedQuantity = sellQuantity+1;
        when(mockPortfolio.getQuantity(mockTicker.getSymbol())).thenReturn(ownedQuantity);
        when(mockPortfolio.getBalance()).thenReturn(initialBalance);

        PortfolioService service = new PortfolioService(mockRepository, mockOrderService, mockTickerService);
        Assertions.assertTrue(service.sell(mockPortfolioId, mockTickerMic, closePrice, sellQuantity));
        verify(mockPortfolio, times(1)).setQuantity(mockTicker, ownedQuantity-sellQuantity);
        verify(mockPortfolio, times(1)).setBalance(initialBalance+closePrice*sellQuantity);
        verify(mockRepository, times(1)).saveAndFlush(mockPortfolio);
    }

    @Test
    public void cantSell(){
        // Affordable case
        int initialBalance = 100;
        int sellQuantity = 10;
        int ownedQuantity = sellQuantity-1;
        when(mockPortfolio.getQuantity(mockTicker.getSymbol())).thenReturn(ownedQuantity);
        when(mockPortfolio.getBalance()).thenReturn(initialBalance);

        PortfolioService service = new PortfolioService(mockRepository, mockOrderService, mockTickerService);
        Assertions.assertFalse(service.sell(mockPortfolioId, mockTickerMic, closePrice, sellQuantity));
        verify(mockPortfolio, never()).setQuantity(any(), anyInt());
        verify(mockPortfolio, never()).setBalance(anyInt());
        verify(mockRepository, never()).saveAndFlush(mockPortfolio);
    }
}
