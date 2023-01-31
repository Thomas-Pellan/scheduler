package fr.pellan.scheduler.service;

import fr.pellan.scheduler.repository.ScheduledTaskRepository;
import fr.pellan.scheduler.util.HttpUtil;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ThreadPoolServiceTest {

    @Mock
    private HttpUtil httpUtil;

    @Mock
    private ScheduledTaskRepository scheduledTaskRepository;

    @Mock
    private ScheduledTaskInputService scheduledTaskInputService;

    @Mock
    private ScheduledTaskOutputService scheduledTaskOutputService;
}
