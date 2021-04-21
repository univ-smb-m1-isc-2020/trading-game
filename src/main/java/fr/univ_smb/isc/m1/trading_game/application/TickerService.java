package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TickerService {
    public List<Ticker> tickers(){
        ArrayList<Ticker>  res = new ArrayList<>();//TODO get from repository
        res.add(new Ticker());
        res.add(new Ticker());
        return res;
    }

    public Ticker get(String mic){
        return new Ticker();
    }
}
