package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Service
public class TaskSchedulerService {
    private final TaskScheduler scheduler;
    private ScheduledFuture<?> scheduledTask;

    public TaskSchedulerService(TaskScheduler scheduler){
        this.scheduler = scheduler;
    }

    public void scheduleTask(Runnable task, CronTrigger cron){
        scheduledTask = scheduler.schedule(task, cron);
    }

    public void stop(){
        scheduledTask.cancel(false);
    }
}
