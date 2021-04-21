package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EODService {
    public EOD getLastEod(Date date){
        return new EOD();//TODO get from repository
    }
}
