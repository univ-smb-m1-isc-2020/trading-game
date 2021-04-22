package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
        when(mockRepository.getOne(mockPlayerId)).thenReturn(mockPlayer);
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
}
