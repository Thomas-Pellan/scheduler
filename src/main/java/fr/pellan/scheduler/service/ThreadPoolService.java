package fr.pellan.scheduler.service;

import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.task.RunnableTask;
import fr.pellan.scheduler.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreadPoolService {

    private final HttpUtil httpUtil;

    private final ScheduledTaskService scheduledTaskService;

    private final ScheduledTaskInputService scheduledTaskInputService;

    private final ScheduledTaskOutputService scheduledTaskOutputService;

    private final ThreadPoolTaskScheduler taskScheduler;

    private ArrayList<ScheduledFuture<?>> tasks = new ArrayList<>();

    public void purgeThreadTasks(){

        log.info("purgeThreadTasks : cancelling {} tasks", tasks.size());

        //Clean tasks
        tasks.forEach(this::waitAndCancelTask);
        tasks = new ArrayList<>();

        log.info("purgeThreadTasks : purged");
    }

    private void waitAndCancelTask(ScheduledFuture<?> task){
        if(!task.isDone()){
            try {
                log.info("waitAndCancelTask : waiting for task to end");
                task.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("waitAndCancelTask : task failed before cancel", e);
                Thread.currentThread().interrupt();
            }
        }
        task.cancel(false);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initThreadTasks(){

        log.info("initThreadTasks : getting active tasks");
        List<ScheduledTaskEntity> activeTasks = scheduledTaskService.findActive();
        if (CollectionUtils.isEmpty(activeTasks)) {
            return;
        }

        log.info("initThreadTasks : creating {} tasks", activeTasks.size());

        //Creating tasks
        activeTasks.forEach(this::addScheduledTaskToThreadPool);
    }

    private void addScheduledTaskToThreadPool(ScheduledTaskEntity task){
        if(task.getCronExpression() == null || StringUtils.isBlank(task.getCronExpression().getCronPattern())){
            return;
        }

        CronTrigger cronTrigger = new CronTrigger(task.getCronExpression().getCronPattern());
        RunnableTask runnable = new RunnableTask(httpUtil, task, scheduledTaskInputService, scheduledTaskService, scheduledTaskOutputService);

        tasks.add(taskScheduler.schedule(runnable, cronTrigger));
    }
}
