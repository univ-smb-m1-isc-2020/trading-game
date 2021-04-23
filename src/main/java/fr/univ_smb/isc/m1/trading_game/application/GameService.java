package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Game;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.GameRepository;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Player;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {
    private final static String TIMEZONE = "Europe/Paris";
    private final GameRepository repository;
    private final PlayerService playerService;
    private final EODService eodService;

    public GameService(GameRepository repository, PlayerService playerService, EODService eodService) {
        this.repository = repository;
        this.playerService = playerService;
        this.eodService = eodService;
    }

    public Game createGame(int maxPortfolios, int initialBalance, int transactionFee, Date startDate, int totalDuration){
        Game game = new Game(maxPortfolios, initialBalance, transactionFee, startDate, totalDuration);
        repository.save(game);
        return game;
    }

    public void addPlayer(long gameId, long playerId){
        Game game = repository.findById(gameId).orElse(null);//TODO test game not existing
        if(game==null) return;
        Player player = playerService.getPlayer(playerId);
        game.getPlayers().add(player);
        repository.save(game);
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
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE));
        calendar.setTime(startDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.add(Calendar.DATE, currentDuration+1);
        return calendar.getTime();
    }
}
