package fr.pellan.scheduler.service;

import com.google.gson.JsonObject;
import fr.pellan.scheduler.dto.ScheduledTaskInputDTO;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.entity.ScheduledTaskInputEntity;
import fr.pellan.scheduler.factory.ScheduledTaskInputDTOFactory;
import fr.pellan.scheduler.factory.ScheduledTaskInputEntityFactory;
import fr.pellan.scheduler.repository.ScheduledTaskInputRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduledTaskInputServiceTest {

    @InjectMocks
    ScheduledTaskInputService scheduledTaskInputService;

    @Mock
    ScheduledTaskInputRepository scheduledTaskInputRepository;

    @Mock
    ScheduledTaskInputEntityFactory scheduledTaskInputEntityFactory;

    @Mock
    ScheduledTaskInputDTOFactory scheduledTaskInputDTOFactory;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenEmptyInputs_whenCreateInputs_returnEmptyList(){

        assertEquals(Collections.EMPTY_LIST, scheduledTaskInputService.createInputs(null, Collections.EMPTY_LIST));
    }

    @Test
    void givenValidInputs_whenCreateInputs_sendBackDtoList(){

        ScheduledTaskInputDTO dummyInputDto1 = new ScheduledTaskInputDTO("key1", "value1");
        ScheduledTaskInputDTO dummyInputDto2 = new ScheduledTaskInputDTO("key2", "value2");
        List<ScheduledTaskInputDTO> inputDtoDummies = List.of(dummyInputDto1, dummyInputDto2);
        ScheduledTaskInputEntity dummyInput1 = new ScheduledTaskInputEntity(null, null, "key1", "value1");
        ScheduledTaskInputEntity dummyInput2 = new ScheduledTaskInputEntity(null, null, "key2", "value2");
        List<ScheduledTaskInputEntity> inputDummies = List.of(dummyInput1, dummyInput2);
        ScheduledTaskEntity dummyTaskEntity = new ScheduledTaskEntity(null, "testTask", null, true, "", null, null, null);


        when(scheduledTaskInputEntityFactory.buildScheduledTaskInputEntity(Mockito.any(List.class))).thenReturn(inputDummies);
        when(scheduledTaskInputRepository.saveAll(Mockito.any(List.class))).then(new Answer<List<ScheduledTaskInputEntity>>() {
            int sequence = 1;

            @Override
            public List<ScheduledTaskInputEntity> answer(InvocationOnMock invocation) {
                List<ScheduledTaskInputEntity> input = invocation.getArgument(0);
                input.forEach(i -> i.setId(sequence++));
                return input;
            }
        });
        when(scheduledTaskInputDTOFactory.buildScheduledTaskInputDTO(Mockito.any(List.class))).thenReturn(inputDtoDummies);

        List<ScheduledTaskInputDTO> inputDtosResult = scheduledTaskInputService.createInputs(dummyTaskEntity, inputDtoDummies);

        verify(scheduledTaskInputEntityFactory).buildScheduledTaskInputEntity(inputDtoDummies);
        verify(scheduledTaskInputRepository).saveAll(inputDummies);
        verify(scheduledTaskInputDTOFactory).buildScheduledTaskInputDTO(inputDummies);
        assertNotNull(inputDtosResult);
        assertEquals(inputDtoDummies, inputDtosResult);
    }

    @Test
    void givenEmptyInputs_whenDeleteInputs_DoNothing(){

        ScheduledTaskEntity dummyTaskEntity = new ScheduledTaskEntity(null, "testTask", null, true, "", null, null, null);
        when(scheduledTaskInputRepository.findByTask(Mockito.any(ScheduledTaskEntity.class))).thenReturn(null);

        scheduledTaskInputService.deleteInputs(dummyTaskEntity);
        verify(scheduledTaskInputRepository).findByTask(dummyTaskEntity);
    }

    @Test
    void givenInputs_whenDeleteInputs_callDeleteAll(){

        ScheduledTaskEntity dummyTaskEntity = new ScheduledTaskEntity(null, "testTask", null, true, "", null, null, null);
        ScheduledTaskInputEntity dummyInput1 = new ScheduledTaskInputEntity(null, null, "key1", "value1");
        ScheduledTaskInputEntity dummyInput2 = new ScheduledTaskInputEntity(null, null, "key2", "value2");
        List<ScheduledTaskInputEntity> inputDummies = List.of(dummyInput1, dummyInput2);

        when(scheduledTaskInputRepository.findByTask(Mockito.any(ScheduledTaskEntity.class))).thenReturn(inputDummies);

        scheduledTaskInputService.deleteInputs(dummyTaskEntity);
        verify(scheduledTaskInputRepository).findByTask(dummyTaskEntity);
        verify(scheduledTaskInputRepository, times(1)).deleteAll(inputDummies);
    }

    @Test
    void givenEmptyInputs_buildEmptyJson(){

        assertNull(scheduledTaskInputService.buildJsonBodyData(null));
    }

    @Test
    void giveInputs_buildJsonFromIt(){

        JsonObject jsonDummy = new JsonObject();
        jsonDummy.addProperty("key", "value");
        jsonDummy.addProperty("key2", "value2");
        ScheduledTaskInputEntity dummyInput1 = new ScheduledTaskInputEntity(null, null, "key", "value");
        ScheduledTaskInputEntity dummyInput2 = new ScheduledTaskInputEntity(null, null, "key2", "value2");

        assertEquals(jsonDummy, scheduledTaskInputService.buildJsonBodyData(List.of(dummyInput1, dummyInput2)));
    }
}
