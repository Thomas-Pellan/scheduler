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
class ScheduledTaskInputDTOFactoryTest {

    @InjectMocks
    ScheduledTaskInputDTOFactory scheduledTaskInputDTOFactory;

    @Test
    void givenNullObjectOrList_returnNullObjectOrList(){

        assertNull(scheduledTaskInputDTOFactory.buildScheduledTaskInputDTO((ScheduledTaskInputEntity) null));
        assertEquals(Collections.EMPTY_LIST, scheduledTaskInputDTOFactory.buildScheduledTaskInputDTO((List<ScheduledTaskInputEntity>) null));
    }

    @Test
    void givenValidObjects_returnValidDTOs(){

        ScheduledTaskInputEntity dummyEntity = new ScheduledTaskInputEntity(1, null, "testkey", "testvalue");
        ScheduledTaskInputDTO dummyDTO = new ScheduledTaskInputDTO("testkey", "testvalue");

        assertEquals(dummyDTO, scheduledTaskInputDTOFactory.buildScheduledTaskInputDTO(dummyEntity));
        assertEquals(List.of(dummyDTO, dummyDTO), scheduledTaskInputDTOFactory.buildScheduledTaskInputDTO(List.of(dummyEntity, dummyEntity)));
    }
}
