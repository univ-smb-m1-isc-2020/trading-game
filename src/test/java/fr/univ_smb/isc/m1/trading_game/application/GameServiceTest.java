package fr.univ_smb.isc.m1.trading_game.application;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Game;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.GameRepository;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

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
        when(mockRepository.findById(anyLong())).thenReturn(Optional.of(mockGame));

        mockPlayerService = mock(PlayerService.class);
        mockEodService = mock(EODService.class);
    }

    @Test
    public void getNeededDateHourMinute(){
        String timezone = "Europe/Paris";
        int year = 2000;
        int month = Calendar.JANUARY;
        int date = 10;
        int hour = 12;
        int min = 30;
        int duration = 1;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timezone));
        calendar.set(year,month,date,hour,min);
        when(mockGame.getStartDate()).thenReturn(calendar.getTime());
        when(mockGame.getCurrentDuration()).thenReturn(duration);

        GameService service = new GameService(mockRepository, mockPlayerService, mockEodService);

        Date neededDate = service.getNeededDate(mockGame);

        calendar.setTime(neededDate);
        Assertions.assertEquals(year, calendar.get(Calendar.YEAR));
        Assertions.assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
        Assertions.assertEquals(date+duration+1, calendar.get(Calendar.DATE));
        Assertions.assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        Assertions.assertEquals(0, calendar.get(Calendar.MINUTE));
    }

    @Test
    public void createGame(){
        int ports = 4;
        int balance = 500;
        int fee = 2;
        Date date = new Date();
        int duration = 20;
        GameService service = new GameService(mockRepository, mockPlayerService, mockEodService);
        Game g = service.createGame(ports, balance, fee, date, duration);
        verify(mockRepository,times(1)).save(g);
        Assertions.assertEquals(ports, g.getMaxPortfolios());
        Assertions.assertEquals(balance, g.getInitialBalance());
        Assertions.assertEquals(fee, g.getTransactionFee());
        Assertions.assertEquals(date, g.getStartDate());
        Assertions.assertEquals(duration, g.getTotalDuration());
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
        Assertions.assertTrue(players.contains(mockPlayer));
        Assertions.assertEquals(1, players.size());
        verify(mockRepository, times(1)).save(mockGame);
    }

    @Test
    public void applyDayData(){
        int duration = 5;
        int playerCount = 4;
        int EODCount = 10;

        ArrayList<Player> players = new ArrayList<>();
        ArrayList<EOD> eods = new ArrayList<>();

        for(int i=0; i<playerCount; i++){
            Player p = mock(Player.class);
            when(p.getId()).thenReturn((long)i);
            players.add(p);
        }

        for(int i=0; i<EODCount; i++){
            EOD eod = mock(EOD.class);
            when(eod.getId()).thenReturn((long)i);
            eods.add(eod);
        }

        when(mockEodService.getEODs(any())).thenReturn(eods);

        when(mockGame.getPlayers()).thenReturn(players);
        when(mockGame.getStartDate()).thenReturn(new Date());
        when(mockGame.getCurrentDuration()).thenReturn(duration);

        GameService service = new GameService(mockRepository, mockPlayerService, mockEodService);
        service.applyDayData(mockGameId);

        verify(mockGame, times(1)).setCurrentDuration(duration+1);
        for(int i=0; i<playerCount; i++){
            for(int j=0; j<EODCount; j++){
                verify(mockPlayerService, times(1)).applyOrders(players.get(i).getId(), eods.get(j));
            }
        }
        verify(mockRepository, times(1)).save(mockGame);
    }

}
