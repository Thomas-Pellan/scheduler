package fr.pellan.scheduler.factory;

import fr.pellan.scheduler.dto.ScheduledTaskDTO;
import fr.pellan.scheduler.entity.CronExpressionEntity;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ScheduledTaskDTOFactoryTest {

    @InjectMocks
    ScheduledTaskDTOFactory scheduledTaskDTOFactory;

    @Test
    void givenNullObjectOrList_returnNullObjectOrList(){

        assertNull(scheduledTaskDTOFactory.buildScheduledTaskDTO((ScheduledTaskEntity) null));
        assertEquals(Collections.EMPTY_LIST, scheduledTaskDTOFactory.buildScheduledTaskDTO((List<ScheduledTaskEntity>) null));
    }

    @Test
    void givenValidObjects_returnValidDTOs(){

        ScheduledTaskEntity dummyEntity = new ScheduledTaskEntity(null, "test", new CronExpressionEntity(null, "* * * * * *"), true, "", null, "", null, null);
        ScheduledTaskDTO dummyDTO = new ScheduledTaskDTO(null, "test","* * * * * *", true, "", null, "", null);

        assertEquals(dummyDTO, scheduledTaskDTOFactory.buildScheduledTaskDTO(dummyEntity));
        assertEquals(List.of(dummyDTO, dummyDTO), scheduledTaskDTOFactory.buildScheduledTaskDTO(List.of(dummyEntity, dummyEntity)));
    }
}
