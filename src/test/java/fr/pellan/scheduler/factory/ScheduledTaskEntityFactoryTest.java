package fr.pellan.scheduler.factory;

import fr.pellan.scheduler.dto.ScheduledTaskDTO;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ScheduledTaskEntityFactoryTest {

    @InjectMocks
    ScheduledTaskEntityFactory scheduledTaskEntityFactory;

    @Test
    void givenNull_returnNullObject(){

        assertNull(scheduledTaskEntityFactory.buildScheduledTaskEntity(null));
    }

    @Test
    void givenValidDto_returnNewObject(){

        ScheduledTaskEntity dummyEntity = new ScheduledTaskEntity(null, "test", null, true, "", null, null, null);
        ScheduledTaskDTO dummyDTO = new ScheduledTaskDTO(null, "test","* * * * * *", true, "", null, null, null);

        assertEquals(dummyEntity, scheduledTaskEntityFactory.buildScheduledTaskEntity(dummyDTO));
    }
}
