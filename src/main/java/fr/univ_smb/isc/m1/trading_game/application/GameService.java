package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Game;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.GameRepository;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {
    private GameRepository repository;
    private PlayerService playerService;

    public void addPlayer(long gameId, Player p){
        Game game = repository.getOne(gameId);
        game.getPlayers().add(p);
        repository.save(game);
    }

    public void applyDayData(long gameId){
        List<EOD> dayData = new ArrayList<>();//TODO EOD service
        Game game = repository.getOne(gameId);
        for(EOD eod : dayData){
            for(Player p : game.getPlayers()){
                playerService.applyOrders(p.getId(),eod);
            }
        }
        game.setCurrentDuration(game.getCurrentDuration()+1);
        repository.save(game);
    }
}
