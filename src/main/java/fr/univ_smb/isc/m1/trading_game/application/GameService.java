package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Service
public class GameService {
    private final static String DEFAULT_TIMEZONE = "GMT";
    private final static Map<Long, ScheduledFuture<?>> RUNNING_GAMES = new HashMap<>();
    private final TaskScheduler scheduler;
    private final GameRepository repository;
    private final PlayerService playerService;
    private final EODService eodService;

    public GameService(TaskScheduler scheduler, GameRepository repository, PlayerService playerService, EODService eodService) {
        this.scheduler = scheduler;
        this.repository = repository;
        this.playerService = playerService;
        this.eodService = eodService;
    }

    public Game createGame(int maxPortfolios, int initialBalance, int transactionFee, Date startDate, int totalDuration){
        Game game = new Game(maxPortfolios, initialBalance, transactionFee, startDate, totalDuration);
        repository.save(game);
        return game;
    }

    public void addPlayer(long gameId, TradingGameUser user){
        Game game = repository.findById(gameId).orElse(null);//TODO test game not existing
        if(game==null) return;
        Player player = playerService.createPlayer(user, game.getMaxPortfolios(), game.getInitialBalance());
        game.getPlayers().add(player);
        repository.save(game);
    }

    public void startGame(long gameId, int dayDurationInSeconds){
        Game game = repository.findById(gameId).orElse(null);//TODO test game not existing
        if(game==null) return;
        if(RUNNING_GAMES.get(gameId) != null) return;
        Runnable gameRunnable = () -> {
            this.applyDayData(gameId);
            this.endGameTaskIfNecessary(gameId);
        };
        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(gameRunnable, Duration.ofSeconds(dayDurationInSeconds));
        addScheduledTask(gameId, task);
    }

    public void addScheduledTask(long gameId,ScheduledFuture<?> task){
        RUNNING_GAMES.put(gameId, task);
    }

    public void endGameTaskIfNecessary(long gameId){
        Game game = repository.findById(gameId).orElse(null);
        if(game==null){
            RUNNING_GAMES.remove(gameId);
            return;
        }
        if(game.hasEnded()){
            ScheduledFuture<?> task = RUNNING_GAMES.get(gameId);
            if(task!=null){
                task.cancel(false);
                RUNNING_GAMES.remove(gameId);
            }
        }
    }

    public void applyDayData(long gameId){
        Game game = repository.findById(gameId).orElse(null);
        List<EOD> dayData = eodService.getEODs(getNeededDate(game));
        if(game == null) return;
        for(EOD eod : dayData){
            for(Player p : game.getPlayers()){
                playerService.applyOrders(p.getId(),eod);
            }
        }
        game.setCurrentDuration(game.getCurrentDuration()+1);
        repository.save(game);
    }

    public Date getNeededDate(Game game){
        if(game==null) return null;
        Date startDate = game.getStartDate();
        int currentDuration = game.getCurrentDuration();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        calendar.setTime(startDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, currentDuration+1);
        return calendar.getTime();
    }

    public static void reset(){
        for(ScheduledFuture<?> task : RUNNING_GAMES.values()){
            if(task!=null) task.cancel(true);
        }
        RUNNING_GAMES.clear();
    }
}
