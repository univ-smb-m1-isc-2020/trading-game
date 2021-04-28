package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EODRepository;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class EODService {
    private final EODRepository repository;

    public EODService(EODRepository repository){
        this.repository = repository;
    }

    public EOD getLast(Ticker ticker) {
        return repository.findTopBySymbolOrderByDate(ticker).orElse(null);
    }

    public List<EOD> getEODs(Date date){
        return repository.findAllByDate(date);
    }

    // TODO : voir si on supprime ou non
    public List<EOD> getEODsByTicker(Ticker ticker){
        return repository.findAllBySymbol(ticker);
    }
}
