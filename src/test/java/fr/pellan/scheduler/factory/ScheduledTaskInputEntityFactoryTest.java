package fr.pellan.scheduler.factory;

import fr.pellan.scheduler.dto.ScheduledTaskInputDTO;
import fr.pellan.scheduler.entity.ScheduledTaskInputEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ScheduledTaskInputEntityFactoryTest {

    @InjectMocks
    ScheduledTaskInputEntityFactory scheduledTaskInputEntityFactory;

    @Test
    void givenNull_returnNullObject(){

        assertNull(scheduledTaskInputEntityFactory.buildScheduledTaskInputEntity((ScheduledTaskInputDTO) null));
        assertEquals(Collections.EMPTY_LIST, scheduledTaskInputEntityFactory.buildScheduledTaskInputEntity((List<ScheduledTaskInputDTO>) null));
    }

    @Test
    void givenValidDto_returnNewObject(){

        ScheduledTaskInputEntity dummyEntity = new ScheduledTaskInputEntity(null, null, "key", "value");
        ScheduledTaskInputDTO dummyDTO = new ScheduledTaskInputDTO("key", "value");

        assertEquals(dummyEntity, scheduledTaskInputEntityFactory.buildScheduledTaskInputEntity(dummyDTO));
        assertEquals(List.of(dummyEntity, dummyEntity), scheduledTaskInputEntityFactory.buildScheduledTaskInputEntity(List.of(dummyDTO, dummyDTO)));
    }
}
