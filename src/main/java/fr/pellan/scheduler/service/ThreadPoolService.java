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

import java.util.List;

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

    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private static final int POOL_SIZE= 5;

    public void reloadThreadTasks(){

        log.info("reloadThreadTasks : call");
        if(threadPoolTaskScheduler != null){
            threadPoolTaskScheduler.destroy();
            log.info("reloadThreadTasks : destroyed");
        }

        initThreadTasks();
    }

    @EventListener(ApplicationReadyEvent.class)
    private void initThreadTasks(){

        log.info("initThreadTasks : call");
        List<ScheduledTaskEntity> tasks = scheduledTaskRepository.findActive();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }

        log.info("initThreadTasks : creating {} tasks", tasks.size());

        //Init cron scheduler
        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        threadPoolTaskScheduler.initialize();

        //Creating tasks
        tasks.forEach(t -> {
            if(t.getCronExpression() == null || StringUtils.isBlank(t.getCronExpression().getCronPattern())){
                return;
            }

            CronTrigger cronTrigger
                    = new CronTrigger(t.getCronExpression().getCronPattern());

            threadPoolTaskScheduler.schedule(new RunnableTask(httpUtil, t, scheduledTaskInputService, scheduledTaskRepository, scheduledTaskOutputService), cronTrigger);
        });
    }
}
