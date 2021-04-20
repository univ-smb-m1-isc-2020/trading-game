package fr.univ_smb.isc.m1.trading_game.application;

import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledFuture;

public class GameTaskScheduler {
    private final TaskScheduler scheduler;
    private ScheduledFuture<?> scheduledTask;

    public GameTaskScheduler(){
        this.scheduler = new TaskSchedulerBuilder().build();
    }

    public void scheduleTask(Runnable task, CronTrigger cron){
        scheduledTask = scheduler.schedule(task, cron);
    }

    public void cancel(){
        scheduledTask.cancel(false);
    }
}
