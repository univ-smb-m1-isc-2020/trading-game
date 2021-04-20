package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;

import java.lang.reflect.Array;
import java.util.*;

public class GameScheduler {
    private final Game game;

    public GameScheduler(Game game){
        this.game = game;
    }

    private void applyOneDay(){
        Date currentDate = game.getCurrentDate();
        Date dataDate = getLastDataDate(currentDate);
        List<EOD> eods = new ArrayList<>();//TODO get it from DB
        game.applyDayData(eods);
    }

    private Date getLastDataDate(Date currentDate){
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

}
