package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Game;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    private PlayerService playerService;

    public void addPlayer(long gameId, Player p){
        Game game = new Game();//TODO repository
        game.getPlayers().add(p);// TODO repo ?
    }

    public void applyDayData(){
        List<EOD> dayData = new ArrayList<>();//TODO EOD service
        Game game = new Game();//TODO repository
        for(EOD eod : dayData){
            for(Player p : game.getPlayers()){
                playerService.applyOrders(p.getId(),eod);
            }
        }
        game.setCurrentDuration(game.getCurrentDuration()+1);;
    }
}
