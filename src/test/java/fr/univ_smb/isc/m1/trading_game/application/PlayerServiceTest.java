package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

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
    public void getPlayer(){
        PlayerService service = new PlayerService(mockRepository, mockPortfolioService);
        Assertions.assertEquals(mockPlayer,service.getPlayer(mockPlayer.getId()));
    }

    @Test
    public void applyOrders(){
        int portfolioCount = 3;

        ArrayList<Portfolio> ports = new ArrayList<>();
        for(int i=0; i<portfolioCount; i++){
            Portfolio p = mock(Portfolio.class);
            when(p.getId()).thenReturn((long)i);
            ports.add(p);
        }
        EOD mockData = mock(EOD.class);
        when(mockPlayer.getPortfolios()).thenReturn(ports);

        PlayerService service = new PlayerService(mockRepository, mockPortfolioService);
        service.applyOrders(mockPlayerId, mockData);

        for(int i=0; i<portfolioCount; i++){
            Portfolio p = ports.get(i);
            verify(mockPortfolioService, times(1)).applyOrders(p.getId(), mockData);
        }
    }

    @Test
    public void getPortfolios(){
        int portfolioCount = 3;
        ArrayList<Portfolio> ports = new ArrayList<>();
        for(int i=0; i<portfolioCount; i++){
            Portfolio p = mock(Portfolio.class);
            when(p.getId()).thenReturn((long)i);
            ports.add(p);
        }
        when(mockPlayer.getPortfolios()).thenReturn(ports);
        PlayerService service = new PlayerService(mockRepository, mockPortfolioService);
        Assertions.assertEquals(ports, service.getPortfolios(mockPlayerId));
    }

    @Test
    public void getTotalBalance(){
        int portfolioCount = 3;
        int[] portfolioAmounts = new int[]{10,50,78};
        ArrayList<Portfolio> ports = new ArrayList<>();
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
