package fr.univ_smb.isc.m1.trading_game.application.services;

import fr.univ_smb.isc.m1.trading_game.application.PlayerService;
import fr.univ_smb.isc.m1.trading_game.application.PortfolioService;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.*;

public class PlayerServiceTest {
    private final static long mockPlayerId = 0;
    private Player mockPlayer;
    private PlayerRepository mockRepository;
    private PortfolioService mockPortfolioService;

    @BeforeEach
    public void setup(){
        mockPlayer = mock(Player.class);
        when(mockPlayer.getId()).thenReturn(mockPlayerId);
        mockRepository = mock(PlayerRepository.class);
        when(mockRepository.findById(mockPlayerId)).thenReturn(Optional.of(mockPlayer));
        mockPortfolioService = mock(PortfolioService.class);
    }


    @Test
    public void createPlayer(){
        int portfolioCount = 2;
        int initialBalance = 100;
        TradingGameUser mockUser = mock(TradingGameUser.class);
        Portfolio port = mock(Portfolio.class);
        Portfolio port2 = mock(Portfolio.class);
        when(port.getBalance()).thenReturn(initialBalance);
        when(port2.getBalance()).thenReturn(initialBalance);
        when(mockPortfolioService.create(initialBalance)).thenReturn(port).thenReturn(port2);

        PlayerService service = new PlayerService(mockRepository, mockPortfolioService);
        Player createdPlayer = service.createPlayer(mockUser, portfolioCount, initialBalance);

        Assertions.assertNotNull(createdPlayer.getPortfolios());
        Assertions.assertEquals(createdPlayer.getPortfolios().size(), portfolioCount);
        for(Portfolio p : createdPlayer.getPortfolios()){
            Assertions.assertEquals(initialBalance, p.getBalance());
        }
        verify(mockRepository, times(1)).saveAndFlush(createdPlayer);
    }

    @Test
    public void getPlayer(){
        PlayerService service = new PlayerService(mockRepository, mockPortfolioService);
        Assertions.assertEquals(mockPlayer,service.getPlayer(mockPlayer.getId()));
    }

    @Test
    public void applyOrders(){
        int portfolioCount = 3;

        Set<Portfolio> ports = new HashSet<>();
        for(int i=0; i<portfolioCount; i++){
            Portfolio p = mock(Portfolio.class);
            when(p.getId()).thenReturn((long)i);
            ports.add(p);
        }
        EOD mockData = mock(EOD.class);
        when(mockPlayer.getPortfolios()).thenReturn(ports);

        PlayerService service = new PlayerService(mockRepository, mockPortfolioService);
        service.applyOrders(mockPlayerId, mockData);

        for(Portfolio p : ports){
            verify(mockPortfolioService, times(1)).applyOrders(p.getId(), mockData);
        }
    }

    @Test
    public void getPortfolios(){
        int portfolioCount = 3;
        Set<Portfolio> ports = new HashSet<>();
        for(int i=0; i<portfolioCount; i++){
            Portfolio p = mock(Portfolio.class);
            when(p.getId()).thenReturn((long)i);
            ports.add(p);
        }
        when(mockPlayer.getPortfolios()).thenReturn(ports);
        PlayerService service = new PlayerService(mockRepository, mockPortfolioService);
        Assertions.assertEquals(ports, new HashSet<>(service.getPortfolios(mockPlayerId)));
    }

    @Test
    public void getPortfolioNumberPlayerNull(){
        long mockPortfolioId = 0;
        Portfolio mockPortfolio = mock(Portfolio.class);
        when(mockPortfolio.getId()).thenReturn(mockPortfolioId);
        Set<Portfolio> mockPortfolioSet = new HashSet<>();
        mockPortfolioSet.add(mockPortfolio);
        when(mockRepository.findById(mockPlayerId)).thenReturn(Optional.empty());
        when(mockPlayer.getPortfolios()).thenReturn(mockPortfolioSet);

        PlayerService service = new PlayerService(mockRepository, mockPortfolioService);
        Assertions.assertEquals(0, service.getPortfolioNumber(mockPlayerId, mockPortfolioId));
    }

    @Test
    public void getPortfolioNumberWrongPortfolioId(){
        long mockPortfolioId = 0;
        long wrongId = 1;
        Portfolio mockPortfolio = mock(Portfolio.class);
        when(mockPortfolio.getId()).thenReturn(mockPortfolioId);
        Set<Portfolio> mockPortfolioSet = new HashSet<>();
        mockPortfolioSet.add(mockPortfolio);
        when(mockRepository.findById(mockPlayerId)).thenReturn(Optional.empty());
        when(mockPlayer.getPortfolios()).thenReturn(mockPortfolioSet);

        PlayerService service = new PlayerService(mockRepository, mockPortfolioService);
        Assertions.assertEquals(0, service.getPortfolioNumber(mockPlayerId, wrongId));
    }

    @Test
    public void getPortfolioNumber(){
        long mockPortfolioId = 0;
        Portfolio mockPortfolio = mock(Portfolio.class);
        when(mockPortfolio.getId()).thenReturn(mockPortfolioId);
        Set<Portfolio> mockPortfolioSet = new HashSet<>();
        mockPortfolioSet.add(mockPortfolio);
        when(mockRepository.findById(mockPlayerId)).thenReturn(Optional.of(mockPlayer));
        when(mockPlayer.getPortfolios()).thenReturn(mockPortfolioSet);

        PlayerService service = new PlayerService(mockRepository, mockPortfolioService);
        Assertions.assertNotEquals(0, service.getPortfolioNumber(mockPlayerId, mockPortfolioId));
    }

    @Test
    public void getTotalBalance(){
        int portfolioCount = 3;
        int[] portfolioAmounts = new int[]{10,50,78};
        HashSet<Portfolio> ports = new HashSet<>();
        for(int i=0; i<portfolioCount; i++){
            Portfolio p = mock(Portfolio.class);
            when(p.getId()).thenReturn((long)i);
            when(p.getBalance()).thenReturn(portfolioAmounts[i]);
            ports.add(p);
        }
        when(mockPlayer.getPortfolios()).thenReturn(ports);

        PlayerService service = new PlayerService(mockRepository, mockPortfolioService);
        int total = service.getTotalBalance(mockPlayerId);
        Assertions.assertEquals(Arrays.stream(portfolioAmounts).sum(), total);
    }
}
