package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EODService {
    public List<EOD> getEODs(Date date){
        ArrayList<EOD> res = new ArrayList<>();//TODO get from repository
        res.add(new EOD());//TODO get from repository
        return res;//TODO get from repository
    }
}
