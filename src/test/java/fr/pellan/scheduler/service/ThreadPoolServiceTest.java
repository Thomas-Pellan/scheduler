package fr.pellan.scheduler.service;

import fr.pellan.scheduler.entity.CronExpressionEntity;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.repository.ScheduledTaskRepository;
import fr.pellan.scheduler.util.HttpUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ThreadPoolServiceTest {

    @Mock
    ScheduledTaskService scheduledTaskService;

    private ThreadPoolTaskScheduler taskScheduler;

    ThreadPoolService threadPoolService;

    private static final int FULL_POOL_SIZE = 2;

    @BeforeEach
    void init_taskScheduler(){
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(FULL_POOL_SIZE);
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        taskScheduler.afterPropertiesSet();
    }

    @Test
    void givenThreadPoolAndTask_whenAskingForReloadWithActiveTasks_purge(){

        threadPoolService = new ThreadPoolService(new HttpUtil(), scheduledTaskService, new ScheduledTaskInputService(), new ScheduledTaskOutputService(), taskScheduler);
        Runnable dummyTask = mock(Runnable.class);
        CronTrigger dummyTrigger = new CronTrigger("*/1 * * * * *");
        ScheduledFuture<?> future = taskScheduler.schedule(dummyTask, dummyTrigger);
        ArrayList<ScheduledFuture<?>> tasks = new ArrayList<>();
        tasks.add(future);
        Field privateTasksField = null;
        try {
            privateTasksField = ThreadPoolService.class.getDeclaredField("tasks");
            privateTasksField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            fail();
        }

        try {
            privateTasksField.set(threadPoolService, tasks);
        } catch (IllegalAccessException e) {
            fail();
        }

        threadPoolService.purgeThreadTasks();

        assertNotNull(future);
        assertTrue(future.isCancelled());
        assertTrue(future.isDone());
        assertEquals(FULL_POOL_SIZE, taskScheduler.getPoolSize());
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenThreadPool_whenThereAreActiveTasks_createThemAndExecutethem(){

        threadPoolService = new ThreadPoolService(new HttpUtil(), scheduledTaskService, new ScheduledTaskInputService(), new ScheduledTaskOutputService(), taskScheduler);
        ScheduledTaskEntity dummyEntity = new ScheduledTaskEntity();
        dummyEntity.setCronExpression(new CronExpressionEntity(0, "* * * * * *"));
        when(scheduledTaskService.findActive()).thenReturn(List.of(dummyEntity));

        threadPoolService.initThreadTasks();

        verify(scheduledTaskService).findActive();
        try {
            Field privateTasksField = ThreadPoolService.class.getDeclaredField("tasks");
            privateTasksField.setAccessible(true);
            ArrayList<ScheduledFuture<?>> tasks = (ArrayList<ScheduledFuture<?>>) privateTasksField.get(threadPoolService);

            assertEquals(1, tasks.size());
            assertFalse(tasks.get(0).isCancelled());
            assertFalse(tasks.get(0).isDone());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }
    }

    @AfterEach
    void delete_taskScheduler(){

        taskScheduler.destroy();
    }
}
