package fr.univ_smb.isc.m1.trading_game.application.services;

import fr.univ_smb.isc.m1.trading_game.application.EODService;
import fr.univ_smb.isc.m1.trading_game.application.GameService;
import fr.univ_smb.isc.m1.trading_game.application.PlayerService;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.TaskScheduler;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class GameServiceTest {
    private final static long mockGameId = 0;

    private Game mockGame;
    private GameRepository mockRepository;
    private PlayerService mockPlayerService;
    private EODService mockEodService;
    private TaskScheduler mockScheduler;
    private ScheduledFuture<?> mockTask;

    @BeforeEach
    public void setUp(){
        GameService.reset();
        mockGame = mock(Game.class);
        when(mockGame.getId()).thenReturn(mockGameId);


        mockRepository = mock(GameRepository.class);
        when(mockRepository.findById(anyLong())).thenReturn(Optional.of(mockGame));

        mockPlayerService = mock(PlayerService.class);
        mockEodService = mock(EODService.class);
        mockScheduler = mock(TaskScheduler.class);
        mockTask = mock(ScheduledFuture.class);
        doReturn((mockTask)).when(mockScheduler).scheduleAtFixedRate(any(),any());
    }

    @Test
    public void getCurrentlyActiveGames(){
        int exampleDuration = 50;
        List<Game> mockGameSet = new ArrayList<>();
        Game mockActiveGame = mock(Game.class);
        when(mockActiveGame.getTotalDuration()).thenReturn(exampleDuration);
        when(mockActiveGame.getCurrentDuration()).thenReturn(exampleDuration-1);
        mockGameSet.add(mockActiveGame);
        when(mockRepository.findAllActive()).thenReturn(mockGameSet);

        GameService service = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);
        List<Game> res = service.getCurrentlyActiveGames();
        Assertions.assertTrue(res.contains(mockActiveGame));
    }

    @Test
    public void getUnstartedGames(){
        int exampleDuration = 50;
        List<Game> mockGameSet = new ArrayList<>();
        Game unstartedGame = mock(Game.class);
        when(unstartedGame.getTotalDuration()).thenReturn(exampleDuration);
        when(unstartedGame.getCurrentDuration()).thenReturn(0);
        mockGameSet.add(unstartedGame);
        when(mockRepository.findAllActive()).thenReturn(mockGameSet);

        GameService service = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);
        List<Game> res = service.getUnstartedGames();
        Assertions.assertTrue(res.contains(unstartedGame));
    }

    @Test
    public void getActiveGamesOf(){
        int activeUnrelatedGameCount = 5;
        int playerGamesCount = 2;
        TradingGameUser mockUser = mock(TradingGameUser.class);
        List<Game> mockUserGames = new ArrayList<>();
        //Init unrelated games
        for(int i=0;i<activeUnrelatedGameCount;i++){
            mockUserGames.add(mock(Game.class));
        }
        //Init games
        for(int i=0;i<playerGamesCount;i++){
            Game mockGame = mock(Game.class);
            Player mockPlayer = mock(Player.class);
            when(mockPlayer.getUser()).thenReturn(mockUser);
            Set<Player> players = new HashSet<>();
            players.add(mockPlayer);
            when(mockGame.getPlayers()).thenReturn(players);
            mockUserGames.add(mockGame);
        }
        when(mockRepository.findAllActive()).thenReturn(mockUserGames);

        GameService service = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);
        List<Game> res = service.getActiveGamesOf(mockUser);
        Assertions.assertTrue(res.stream().allMatch(game -> game.getPlayers().stream().anyMatch(p -> p.getUser()==mockUser)));
        Assertions.assertEquals(playerGamesCount,res.size());
    }

    @Test
    public void getAvailableGamesOf(){
        int availableGameCount = 5;
        int unavailableGameCount = 2;
        int startedGameCount = 2;
        TradingGameUser mockUser = mock(TradingGameUser.class);
        List<Game> mockUserGames = new ArrayList<>();
        //Init unrelated games
        for(int i=0;i<availableGameCount;i++){
            Game mockGame = mock(Game.class);
            when(mockGame.getCurrentDuration()).thenReturn(0);
            mockUserGames.add(mockGame);
        }
        //Init unrelated games
        for(int i=0;i<startedGameCount;i++){
            Game mockGame = mock(Game.class);
            when(mockGame.getCurrentDuration()).thenReturn(1);
            mockUserGames.add(mockGame);
        }
        //Init games
        for(int i=0;i<unavailableGameCount;i++){
            Game mockGame = mock(Game.class);
            when(mockGame.getCurrentDuration()).thenReturn(0);
            Player mockPlayer = mock(Player.class);
            when(mockPlayer.getUser()).thenReturn(mockUser);
            Set<Player> players = new HashSet<>();
            players.add(mockPlayer);
            when(mockGame.getPlayers()).thenReturn(players);
            mockUserGames.add(mockGame);
        }
        when(mockRepository.findAllActive()).thenReturn(mockUserGames);

        GameService service = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);
        List<Game> res = service.getAvailableGames(mockUser);
        Assertions.assertTrue(res.stream().noneMatch(game -> game.getPlayers().stream().anyMatch(p -> p.getUser()==mockUser)));
        Assertions.assertTrue(res.stream().noneMatch(game -> game.getCurrentDuration() > 0));
        Assertions.assertEquals(availableGameCount,res.size());
    }

    @Test
    public void getEndedGames(){
        int endedGameCount = 5;
        List<Game> mockEndedGames = new ArrayList<>();
        for(int i=0;i<endedGameCount;i++){
            mockEndedGames.add(mock(Game.class));
        }
        when(mockRepository.findAllInactive()).thenReturn(mockEndedGames);
        GameService service = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);

        Assertions.assertEquals(mockEndedGames,service.getEndedGames());
    }

    @Test
    public void getEndedGamesOf(){
        int endedGameCount = 5;
        int playerGamesCount = 2;
        TradingGameUser mockUser = mock(TradingGameUser.class);
        List<Game> mockEndedGames = new ArrayList<>();
        //Init unrelated games
        for(int i=0;i<endedGameCount;i++){
            mockEndedGames.add(mock(Game.class));
        }
        //Init games containing user
        for(int i=0;i<playerGamesCount;i++){
            Game mockGame = mock(Game.class);
            Player mockPlayer = mock(Player.class);
            when(mockPlayer.getUser()).thenReturn(mockUser);
            Set<Player> players = new HashSet<>();
            players.add(mockPlayer);
            when(mockGame.getPlayers()).thenReturn(players);
            mockEndedGames.add(mockGame);
        }
        when(mockRepository.findAllInactive()).thenReturn(mockEndedGames);

        GameService service = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);
        List<Game> res = service.getEndedGamesOf(mockUser);
        Assertions.assertTrue(res.stream().allMatch(game -> game.getPlayers().stream().anyMatch(p -> p.getUser()==mockUser)));
        Assertions.assertEquals(playerGamesCount,res.size());
    }

    @Test
    public void getNeededDateHourMinute(){
        String timezone = "GMT";
        int year = 2000;
        int month = Calendar.JANUARY;
        int date = 10;
        int hour = 12;
        int min = 30;
        int duration = 1;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timezone));
        calendar.clear();
        calendar.set(year,month,date,hour,min);
        when(mockGame.getStartDate()).thenReturn(calendar.getTime());
        when(mockGame.getCurrentDuration()).thenReturn(duration);

        GameService service = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);

        Date neededDate = service.getNeededDate(mockGame);

        calendar.setTime(neededDate);
        Assertions.assertEquals(year, calendar.get(Calendar.YEAR));
        Assertions.assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
        Assertions.assertEquals(date+duration+1, calendar.get(Calendar.DATE));
        Assertions.assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        Assertions.assertEquals(0, calendar.get(Calendar.MINUTE));
        Assertions.assertEquals(0, calendar.get(Calendar.SECOND));
        Assertions.assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    @Test
    public void createGame(){
        int ports = 4;
        int balance = 500;
        int fee = 2;
        Date date = new Date();
        int duration = 20;
        GameService service = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);
        Game g = service.createGame(ports, balance, fee, date, duration);
        verify(mockRepository,times(1)).saveAndFlush(g);
        Assertions.assertEquals(ports, g.getMaxPortfolios());
        Assertions.assertEquals(balance, g.getInitialBalance());
        Assertions.assertEquals(fee, g.getTransactionFee());
        Assertions.assertEquals(date, g.getStartDate());
        Assertions.assertEquals(duration, g.getTotalDuration());
    }

    @Test
    public void addPlayer(){
        long mockPlayerId = 0;
        Integer initBalance = 150;
        Integer portCount = 3;
        HashSet<Player> players = new HashSet<>();
        Player mockPlayer = mock(Player.class);
        TradingGameUser mockUser = mock(TradingGameUser.class);
        when(mockUser.getId()).thenReturn(mockPlayerId);
        when(mockPlayer.getUser()).thenReturn(mockUser);
        when(mockGame.getPlayers()).thenReturn(players);
        when(mockGame.getInitialBalance()).thenReturn(initBalance);
        when(mockGame.getMaxPortfolios()).thenReturn(portCount);
        when(mockPlayerService.createPlayer(mockUser, mockGame.getMaxPortfolios(), mockGame.getInitialBalance())).thenReturn(mockPlayer);

        GameService service = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);
        service.addPlayer(mockGameId, mockUser);
        Assertions.assertEquals(1, players.size());
        Assertions.assertTrue(players.contains(mockPlayer));

        verify(mockRepository, times(1)).saveAndFlush(mockGame);
    }

    @Test
    public void addPlayerAlreadyJoined(){
        long mockPlayerId = 0;
        Integer initBalance = 150;
        Integer portCount = 3;
        HashSet<Player> players = new HashSet<>();
        Player mockPlayer = mock(Player.class);
        players.add(mockPlayer);

        TradingGameUser mockUser = mock(TradingGameUser.class);
        when(mockUser.getId()).thenReturn(mockPlayerId);
        when(mockPlayer.getUser()).thenReturn(mockUser);
        when(mockGame.getPlayers()).thenReturn(players);
        when(mockGame.getInitialBalance()).thenReturn(initBalance);
        when(mockGame.getMaxPortfolios()).thenReturn(portCount);
        when(mockPlayerService.createPlayer(mockUser, mockGame.getMaxPortfolios(), mockGame.getInitialBalance())).thenReturn(mockPlayer);

        GameService service = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);
        service.addPlayer(mockGameId, mockUser);
        Assertions.assertEquals(1, players.size());
        Assertions.assertTrue(players.contains(mockPlayer));
        verify(mockRepository, never()).saveAndFlush(mockGame);
    }

    @Test
    public void addPlayerGameNotExisting(){
        long mockPlayerId = 0;
        Integer initBalance = 150;
        Integer portCount = 3;
        HashSet<Player> players = new HashSet<>();
        Player mockPlayer = mock(Player.class);

        TradingGameUser mockUser = mock(TradingGameUser.class);
        when(mockUser.getId()).thenReturn(mockPlayerId);
        when(mockPlayer.getUser()).thenReturn(mockUser);
        when(mockGame.getPlayers()).thenReturn(players);
        when(mockGame.getInitialBalance()).thenReturn(initBalance);
        when(mockGame.getMaxPortfolios()).thenReturn(portCount);
        when(mockPlayerService.createPlayer(mockUser, mockGame.getMaxPortfolios(), mockGame.getInitialBalance())).thenReturn(mockPlayer);

        when(mockRepository.findById(anyLong())).thenReturn(Optional.empty());
        GameService gameService = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);
        gameService.addPlayer(mockGameId, mockUser);
        // TODO : voir : test du joueur non présent dans la liste peut être inutile
        Assertions.assertEquals(0, players.size());
        Assertions.assertFalse(players.contains(mockPlayer));
        verify(mockRepository, never()).saveAndFlush(mockGame);
    }

    @Test
    public void getPlayer(){
        long mockGameID = 12345L;
        long mockPlayerId = 12345L;
        TradingGameUser mockUser = mock(TradingGameUser.class);
        Player mockPlayer = mock(Player.class);
        HashSet<Player> players = new HashSet<>();
        players.add(mockPlayer);

        when(mockUser.getId()).thenReturn(mockPlayerId);
        when(mockPlayer.getUser()).thenReturn(mockUser);
        when(mockRepository.findById(mockGameID)).thenReturn(Optional.ofNullable(mockGame));
        when(mockGame.getPlayers()).thenReturn(players);

        GameService gameService = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);

        Assertions.assertEquals(mockPlayer, gameService.getPlayer(mockGameID, mockUser));
    }

    @Test
    public void getPlayerNotExistingGame(){
        long mockGameID = 12345L;
        TradingGameUser mockUser = mock(TradingGameUser.class);
        when(mockRepository.findById(mockGameID)).thenReturn(Optional.empty());
        GameService gameService = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);
        Assertions.assertNull(gameService.getPlayer(mockGameID, mockUser));
        verify(mockGame, never()).getPlayers();
    }

    @Test
    public void getPlayerNotExistingPlayer(){
        long mockGameID = 12345L;
        long mockPlayerId = 12345L;
        TradingGameUser mockUserIn = mock(TradingGameUser.class);
        TradingGameUser mockUserOut = mock(TradingGameUser.class);
        Player mockPlayerIn = mock(Player.class);
        HashSet<Player> players = new HashSet<>();
        players.add(mockPlayerIn);

        when(mockUserIn.getId()).thenReturn(mockPlayerId);
        when(mockPlayerIn.getUser()).thenReturn(mockUserIn);
        when(mockRepository.findById(mockGameID)).thenReturn(Optional.ofNullable(mockGame));
        when(mockGame.getPlayers()).thenReturn(players);
        GameService gameService = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);
        Assertions.assertNull(gameService.getPlayer(mockGameID, mockUserOut));
        verify(mockGame).getPlayers();
    }

    @Test
    public void startGame(){
        int duration = 5;
        GameService service = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);
        service.startGame(mockGameId, duration);
        verify(mockScheduler, times(1)).scheduleAtFixedRate(any(), any(), argThat((Duration d) -> d.toSeconds()==duration));
    }

    @Test
    public void startGameNotExistingGame(){
        int duration = 5;
        GameRepository mockEmptyRepository = mock(GameRepository.class);
        when(mockEmptyRepository.findById(mockGameId)).thenReturn(Optional.empty());
        GameService service = new GameService(mockScheduler, mockEmptyRepository, mockPlayerService, mockEodService);
        service.startGame(mockGameId, duration);
        verify(mockScheduler, never()).scheduleAtFixedRate(any(), any(), any());
    }

    @Test
    public void endGameTaskIfNecessary(){
        when(mockRepository.findById(anyLong())).thenReturn(Optional.of(mockGame));
        when(mockGame.hasEnded()).thenReturn(true);
        GameService service = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);
        service.addScheduledTask(mockGameId, mockTask);
        service.endGameTaskIfNecessary(mockGameId);
        verify(mockTask, times(1)).cancel(false);
    }

    @Test
    public void applyDayData(){
        int duration = 5;
        int playerCount = 4;
        int EODCount = 10;

        HashSet<Player> players = new HashSet<>();
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

        GameService service = new GameService(mockScheduler, mockRepository, mockPlayerService, mockEodService);
        service.applyDayData(mockGameId);

        verify(mockGame, times(1)).setCurrentDuration(duration+1);
        for(Player p : players){
            for(int j=0; j<EODCount; j++){
                verify(mockPlayerService, times(1)).applyOrders(p.getId(), eods.get(j));
            }
        }
        verify(mockRepository, times(1)).saveAndFlush(mockGame);
    }

}
