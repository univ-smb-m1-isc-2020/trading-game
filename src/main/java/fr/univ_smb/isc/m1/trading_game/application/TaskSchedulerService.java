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
    private final HashMap<Integer, ScheduledFuture<?>> tasks;
    private int lastId;

    public TaskSchedulerService(TaskScheduler scheduler){
        this.scheduler = scheduler;
        tasks = new HashMap<>();
        lastId = 0;
    }

    public int scheduleTask(Runnable task, CronTrigger cron){
        lastId++;
        tasks.put(lastId, scheduler.schedule(task, cron));
        return lastId;
    }

    public void cancel(int id){
        ScheduledFuture<?> task = tasks.get(id);
        if(task!=null){
            task.cancel(false);
            tasks.remove(id);
        }
    }
}
