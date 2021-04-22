package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TickerRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TickerService {
    private final TickerRepository repository;

    public TickerService(TickerRepository repository){
        this.repository = repository;
    }

    public List<Ticker> tickers(){
        return repository.findAll();
    }

    public Ticker get(String mic){
        return repository.getOne(mic);
    }
}
