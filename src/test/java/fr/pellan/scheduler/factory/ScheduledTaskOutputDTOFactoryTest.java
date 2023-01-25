package fr.pellan.scheduler.factory;

import fr.pellan.scheduler.dto.ScheduledTaskInputDTO;
import fr.pellan.scheduler.dto.ScheduledTaskOutputDTO;
import fr.pellan.scheduler.entity.ScheduledTaskInputEntity;
import fr.pellan.scheduler.entity.ScheduledTaskOutputEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ScheduledTaskOutputDTOFactoryTest {

    @InjectMocks
    ScheduledTaskOutputDTOFactory scheduledTaskOutputDTOFactory;

    @Test
    void givenNullObjectOrList_returnNullObjectOrList(){

        assertNull(scheduledTaskOutputDTOFactory.buildScheduledTaskOutputDTO((ScheduledTaskOutputEntity) null));
        assertEquals(Collections.EMPTY_LIST, scheduledTaskOutputDTOFactory.buildScheduledTaskOutputDTO((List<ScheduledTaskOutputEntity>) null));
    }

    @Test
    void givenValidObjects_returnValidDTOs(){

        ScheduledTaskOutputEntity dummyEntity = new ScheduledTaskOutputEntity(1, null, null, "testdata", "teststate", "testlog");
        ScheduledTaskOutputDTO dummyDTO = new ScheduledTaskOutputDTO(null, "testdata", "teststate", "testlog");

        assertEquals(dummyDTO, scheduledTaskOutputDTOFactory.buildScheduledTaskOutputDTO(dummyEntity));
        assertEquals(List.of(dummyDTO, dummyDTO), scheduledTaskOutputDTOFactory.buildScheduledTaskOutputDTO(List.of(dummyEntity, dummyEntity)));
    }
}
