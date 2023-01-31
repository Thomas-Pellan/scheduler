package fr.pellan.scheduler.service;

import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.repository.ScheduledTaskOutputRepository;
import fr.pellan.scheduler.repository.ScheduledTaskRepository;
import fr.pellan.scheduler.task.RunnableTask;
import fr.pellan.scheduler.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Slf4j
@Service
public class ThreadPoolService {

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private ScheduledTaskRepository scheduledTaskRepository;

    @Autowired
    private ScheduledTaskInputService scheduledTaskInputService;

    @Autowired
    private ScheduledTaskOutputService scheduledTaskOutputService;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    public void reloadThreadTasks(){

        log.info("reloadThreadTasks : call");
        var executor = taskScheduler.getScheduledThreadPoolExecutor();

        //Clean executor queue
        executor.getQueue().forEach(executor::remove);

        //Purge the executor
        executor.purge();

        log.info("reloadThreadTasks : purged");

        initThreadTasks();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initThreadTasks(){

        log.info("initThreadTasks : getting active tasks");
        List<ScheduledTaskEntity> tasks = scheduledTaskRepository.findActive();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }

        log.info("initThreadTasks : creating {} tasks", tasks.size());

        //Creating tasks
        tasks.forEach(this::addScheduledTaskToThreadPool);
    }

    private void addScheduledTaskToThreadPool(ScheduledTaskEntity task){
        if(task.getCronExpression() == null || StringUtils.isBlank(task.getCronExpression().getCronPattern())){
            return;
        }

        CronTrigger cronTrigger = new CronTrigger(task.getCronExpression().getCronPattern());

        taskScheduler.schedule(new RunnableTask(httpUtil, task, scheduledTaskInputService, scheduledTaskRepository, scheduledTaskOutputService), cronTrigger);
    }
}
