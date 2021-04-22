package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Game;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.GameRepository;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class GameServiceTest {
    private final static long mockGameId = 0;

    private Game mockGame;
    private GameRepository mockRepository;
    private PlayerService mockPlayerService;
    private EODService mockEodService;

    @BeforeEach
    public void setUp(){
        mockGame = mock(Game.class);
        when(mockGame.getId()).thenReturn(mockGameId);

        mockRepository = mock(GameRepository.class);
        when(mockRepository.getOne(anyLong())).thenReturn(mockGame);

        mockPlayerService = mock(PlayerService.class);
        mockEodService = mock(EODService.class);
    }

    @Test
    public void addPlayer(){
        long mockPlayerId = 0;
        ArrayList<Player> players = new ArrayList<>();
        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getId()).thenReturn(mockPlayerId);
        when(mockGame.getPlayers()).thenReturn(players);
        when(mockPlayerService.getPlayer(mockPlayerId)).thenReturn(mockPlayer);

        GameService service = new GameService(mockRepository, mockPlayerService, mockEodService);
        service.addPlayer(mockGameId, mockPlayer.getId());
        //Assertions.assertTrue(players.contains(mockPlayer));
        Assertions.assertEquals(1, players.size());
        verify(mockRepository, times(1)).save(mockGame);
    }

    @Test
    public void applyDayData(){

    }

}
