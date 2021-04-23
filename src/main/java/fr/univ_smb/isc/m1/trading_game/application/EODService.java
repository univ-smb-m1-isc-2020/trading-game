package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EODRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EODService {
    private final EODRepository repository;

    public EODService(EODRepository repository){
        this.repository = repository;
    }

    public List<EOD> getEODs(Date date){
        return repository.findAllByDate(date);
    }
}
