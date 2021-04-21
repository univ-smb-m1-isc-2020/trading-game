package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Game;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class GameScheduler {
    private final Game game;
    private GameService gameService;
    private final TaskScheduler scheduler;
    private ScheduledFuture<?> scheduledTask;

    public GameScheduler(Game game){
        this.scheduler = new TaskSchedulerBuilder().build();
        this.game = game;
    }

    public void start(){
        if(scheduledTask!=null) return;
        scheduledTask = scheduler.schedule(this::applyCycle, new CronTrigger("0 0 0 * * ?"));
    }

    public void stop(){
        scheduledTask.cancel(false);
        scheduledTask = null;
    }

    public void applyCycle(){
        List<EOD> data = new ArrayList<>();//TODO get if from db
        ga
        game.applyDayData(data);
    }

}
