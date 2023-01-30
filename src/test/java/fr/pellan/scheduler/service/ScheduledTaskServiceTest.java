package fr.pellan.scheduler.service;

import fr.pellan.scheduler.dto.ScheduledTaskDTO;
import fr.pellan.scheduler.dto.ScheduledTaskInputDTO;
import fr.pellan.scheduler.entity.CronExpressionEntity;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.entity.ScheduledTaskInputEntity;
import fr.pellan.scheduler.factory.ScheduledTaskDTOFactory;
import fr.pellan.scheduler.factory.ScheduledTaskEntityFactory;
import fr.pellan.scheduler.repository.ScheduledTaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduledTaskServiceTest {

    @InjectMocks
    ScheduledTaskService scheduledTaskService;

    @Mock
    ScheduledTaskRepository scheduledTaskRepository;

    @Mock
    CronExpressionService cronExpressionService;

    @Mock
    ScheduledTaskDTOFactory scheduledTaskDTOFactory;

    @Mock
    ScheduledTaskEntityFactory scheduledTaskEntityFactory;

    @Mock
    ThreadPoolService threadPoolService;

    @Mock
    ScheduledTaskInputService scheduledTaskInputService;

    @Test
    void givenStringName_whenSearchForTaskByNameOrUrl_returnTaskMatching(){

        ScheduledTaskDTO dummyDto = new ScheduledTaskDTO(0, "test", null, true, null, null, null, null);
        ScheduledTaskEntity dummy = new ScheduledTaskEntity(0, "test", null, true, null, null, null, null, null);
        when(scheduledTaskRepository.findByNameOrUrl(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(List.of(dummy));
        when(scheduledTaskDTOFactory.buildScheduledTaskDTO(Mockito.any(List.class))).thenReturn(List.of(dummyDto));

        List<ScheduledTaskDTO> result = scheduledTaskService.searchTasks("test", "test");

        assertEquals(List.of(dummyDto), result);
        verify(scheduledTaskRepository).findByNameOrUrl("test", "test");
        verify(scheduledTaskDTOFactory).buildScheduledTaskDTO(List.of(dummy));
    }

    @Test
    void givenTaskId_whenSearchForTaskById_returnTaskMatching(){

        ScheduledTaskDTO dummyDto = new ScheduledTaskDTO(0, "test", null, true, null, null, null, null);
        ScheduledTaskEntity dummy = new ScheduledTaskEntity(0, "test", null, true, null, null, null, null, null);
        when(scheduledTaskRepository.findById(Mockito.any(Integer.class))).thenReturn(Optional.of(dummy));
        when(scheduledTaskDTOFactory.buildScheduledTaskDTO(Mockito.any(ScheduledTaskEntity.class))).thenReturn(dummyDto);

        ScheduledTaskDTO result = scheduledTaskService.findById(0);

        assertEquals(dummyDto, result);
        verify(scheduledTaskRepository).findById(0);
        verify(scheduledTaskDTOFactory).buildScheduledTaskDTO(dummy);
    }

    @Test
    void givenTaskId_whenSearchForAllTasks_returnAllTasks(){

        ScheduledTaskDTO dummyDto = new ScheduledTaskDTO(0, "test", null, true, null, null, null, null);
        ScheduledTaskEntity dummy = new ScheduledTaskEntity(0, "test", null, true, null, null, null, null, null);
        when(scheduledTaskRepository.findAll()).thenReturn(List.of(dummy));
        when(scheduledTaskDTOFactory.buildScheduledTaskDTO(Mockito.any(List.class))).thenReturn(List.of(dummyDto));

        List<ScheduledTaskDTO> result = scheduledTaskService.find();

        assertEquals(List.of(dummyDto), result);
        verify(scheduledTaskRepository).findAll();
        verify(scheduledTaskDTOFactory).buildScheduledTaskDTO(List.of(dummy));
    }

    @Test
    void givenTaskId_whenTaskDoesNotExist_deletionDoesNothing(){

        when(scheduledTaskRepository.findById(Mockito.any(Integer.class))).thenReturn(Optional.empty());

        boolean result = scheduledTaskService.deleteTask(0);

        assertFalse(result);
        verify(scheduledTaskRepository).findById(0);
    }

    @Test
    void givenTaskId_whenTaskDoesExistAndIsInactive_deleteTheTask(){

        ScheduledTaskEntity dummy = new ScheduledTaskEntity(0, "test", null, false, null, null, null, null, null);
        when(scheduledTaskRepository.findById(Mockito.any(Integer.class))).thenReturn(Optional.of(dummy));

        boolean result = scheduledTaskService.deleteTask(0);

        assertTrue(result);
        verify(scheduledTaskRepository).findById(0);
        verify(scheduledTaskRepository).delete(dummy);
    }

    @Test
    void givenTaskId_whenTaskDoesExistAndIsActive_saveTheTaskReloadThreadPoolAndDeleteTheTask(){

        ScheduledTaskEntity dummy = new ScheduledTaskEntity(0, "test", null, true, null, null, null, null, null);
        when(scheduledTaskRepository.findById(Mockito.any(Integer.class))).thenReturn(Optional.of(dummy));
        when(scheduledTaskRepository.save(Mockito.any(ScheduledTaskEntity.class))).thenReturn(dummy);

        boolean result = scheduledTaskService.deleteTask(0);

        assertTrue(result);
        verify(scheduledTaskRepository).findById(0);
        dummy.setActive(false);
        verify(scheduledTaskRepository).save(dummy);
        verify(threadPoolService).reloadThreadTasks();
        verify(scheduledTaskRepository).delete(dummy);
    }

    @Test
    void givenTaskDto_whenTaskDoesNotExist_doNothingOnUpdate(){

        ScheduledTaskDTO dummyDto = mock(ScheduledTaskDTO.class);
        when(scheduledTaskRepository.findById(Mockito.any(Integer.class))).thenReturn(Optional.empty());

        ScheduledTaskDTO result = scheduledTaskService.updateTask(dummyDto);

        assertNull(result);
        verify(scheduledTaskRepository).findById(0);
    }

    @Test
    void givenTaskDto_whenTaskDoesExistButHasEmptyCronExpression_doNothingOnUpdate(){

        ScheduledTaskDTO dummyDto = new ScheduledTaskDTO();
        dummyDto.setId(0);
        dummyDto.setCronExpression(null);
        ScheduledTaskEntity dummy = mock(ScheduledTaskEntity.class);
        when(scheduledTaskRepository.findById(Mockito.any(Integer.class))).thenReturn(Optional.of(dummy));

        ScheduledTaskDTO result = scheduledTaskService.updateTask(dummyDto);

        assertNull(result);
        verify(scheduledTaskRepository).findById(0);
    }

    @Test
    void givenTaskDto_whenTaskDoesExist_updateIt(){

        ScheduledTaskDTO dummyDto = new ScheduledTaskDTO();
        dummyDto.setId(0);
        dummyDto.setCronExpression("* * * * * */12");
        dummyDto.setName("modified");
        dummyDto.setActive(false);
        dummyDto.setUrl("modified");

        ScheduledTaskEntity dummy = new ScheduledTaskEntity();
        dummy.setCronExpression(new CronExpressionEntity(0, "* * * * * */5"));
        dummy.setName("original");
        dummy.setActive(true);
        dummy.setUrl("original");

        CronExpressionEntity dummyExpression = new CronExpressionEntity();
        dummyExpression.setCronPattern("* * * * * */12");

        when(scheduledTaskRepository.findById(Mockito.any(Integer.class))).thenReturn(Optional.of(dummy));
        when(cronExpressionService.findExpression(Mockito.any(String.class))).thenReturn(null);
        when(cronExpressionService.createExpression(Mockito.any(String.class))).thenReturn(dummyExpression);
        when(scheduledTaskRepository.save(Mockito.any(ScheduledTaskEntity.class))).then((Answer<ScheduledTaskEntity>) invocation -> invocation.getArgument(0));
        when(scheduledTaskDTOFactory.buildScheduledTaskDTO(Mockito.any(ScheduledTaskEntity.class))).thenReturn(dummyDto);

        ScheduledTaskDTO result = scheduledTaskService.updateTask(dummyDto);

        verify(scheduledTaskRepository).findById(0);
        verify(cronExpressionService).findExpression("* * * * * */12");
        verify(cronExpressionService).createExpression("* * * * * */12");
        verify(scheduledTaskRepository).save(dummy);
        verify(threadPoolService).reloadThreadTasks();
        verify(scheduledTaskDTOFactory).buildScheduledTaskDTO(dummy);
        assertEquals(dummyDto, result);
    }

    @Test
    void givenTaskDto_whenTaskIsInvalid_doNothing(){

        ScheduledTaskDTO dummyDto = new ScheduledTaskDTO();
        dummyDto.setCronExpression(null);
        dummyDto.setName("new");
        dummyDto.setActive(false);
        dummyDto.setUrl("new");

        when(scheduledTaskEntityFactory.buildScheduledTaskEntity(Mockito.any(ScheduledTaskDTO.class))).thenReturn(null);

        ScheduledTaskDTO result = scheduledTaskService.createTask(dummyDto);

        verify(scheduledTaskEntityFactory).buildScheduledTaskEntity(dummyDto);
        assertNull(result);
    }

    @Test
    void givenTaskDto_whenTaskAlreadyExists_createDoesNothing(){

        ScheduledTaskDTO dummyDto = new ScheduledTaskDTO();
        dummyDto.setCronExpression("* * * * * */12");
        dummyDto.setName("new");
        dummyDto.setActive(false);
        dummyDto.setUrl("new");

        ScheduledTaskEntity dummy = new ScheduledTaskEntity();
        dummy.setName("existing");
        dummy.setActive(true);
        dummy.setUrl("existing");

        when(scheduledTaskEntityFactory.buildScheduledTaskEntity(Mockito.any(ScheduledTaskDTO.class))).thenReturn(dummy);
        when(scheduledTaskRepository.findByName(Mockito.any(String.class))).thenReturn(List.of(dummy));

        ScheduledTaskDTO result = scheduledTaskService.createTask(dummyDto);

        verify(scheduledTaskEntityFactory).buildScheduledTaskEntity(dummyDto);
        verify(scheduledTaskRepository).findByName("new");
        assertNull(result);
    }

    @Test
    void givenTaskDto_whenTaskDoesNotExistsAndWithEmptyInputs_createNewTask(){

        ScheduledTaskDTO dummyDto = new ScheduledTaskDTO();
        dummyDto.setCronExpression("* * * * * */12");
        dummyDto.setName("new");
        dummyDto.setActive(false);
        dummyDto.setUrl("new");

        CronExpressionEntity dummyExpression = new CronExpressionEntity();
        dummyExpression.setCronPattern("* * * * * */12");

        ScheduledTaskEntity dummy = new ScheduledTaskEntity();
        dummy.setCronExpression(dummyExpression);
        dummy.setName("new");
        dummy.setActive(true);
        dummy.setUrl("new");

        when(scheduledTaskEntityFactory.buildScheduledTaskEntity(Mockito.any(ScheduledTaskDTO.class))).thenReturn(dummy);
        when(scheduledTaskRepository.findByName(Mockito.any(String.class))).thenReturn(Collections.emptyList());
        when(cronExpressionService.findExpression(Mockito.any(String.class))).thenReturn(dummyExpression);
        when(scheduledTaskRepository.save(Mockito.any(ScheduledTaskEntity.class))).thenReturn(dummy);
        when(scheduledTaskDTOFactory.buildScheduledTaskDTO(Mockito.any(ScheduledTaskEntity.class))).thenReturn(dummyDto);

        ScheduledTaskDTO result = scheduledTaskService.createTask(dummyDto);

        verify(scheduledTaskEntityFactory).buildScheduledTaskEntity(dummyDto);
        verify(scheduledTaskRepository).findByName("new");
        verify(cronExpressionService).findExpression("* * * * * */12");
        verify(scheduledTaskRepository).save(dummy);
        verify(scheduledTaskDTOFactory).buildScheduledTaskDTO(dummy);
        assertEquals(dummyDto, result);
    }

    @Test
    void givenTaskDto_whenTaskDoesNotExistsAndWithInputs_createNewTaskWithImputs(){

        ScheduledTaskInputDTO inputDummyDto = mock(ScheduledTaskInputDTO.class);

        ScheduledTaskDTO dummyDto = new ScheduledTaskDTO();
        dummyDto.setCronExpression("* * * * * */12");
        dummyDto.setName("new");
        dummyDto.setActive(false);
        dummyDto.setInputs(List.of(inputDummyDto));
        dummyDto.setUrl("new");

        CronExpressionEntity dummyExpression = new CronExpressionEntity();
        dummyExpression.setCronPattern("* * * * * */12");

        ScheduledTaskEntity dummy = new ScheduledTaskEntity();
        dummy.setCronExpression(dummyExpression);
        dummy.setName("new");
        dummy.setActive(true);
        dummy.setUrl("new");

        when(scheduledTaskEntityFactory.buildScheduledTaskEntity(Mockito.any(ScheduledTaskDTO.class))).thenReturn(dummy);
        when(scheduledTaskRepository.findByName(Mockito.any(String.class))).thenReturn(Collections.emptyList());
        when(cronExpressionService.findExpression(Mockito.any(String.class))).thenReturn(dummyExpression);
        when(scheduledTaskRepository.save(Mockito.any(ScheduledTaskEntity.class))).thenReturn(dummy);
        when(scheduledTaskDTOFactory.buildScheduledTaskDTO(Mockito.any(ScheduledTaskEntity.class))).thenReturn(dummyDto);
        when(scheduledTaskInputService.createInputs(Mockito.any(ScheduledTaskEntity.class), Mockito.any(List.class))).thenReturn(List.of(inputDummyDto));

        ScheduledTaskDTO result = scheduledTaskService.createTask(dummyDto);

        verify(scheduledTaskEntityFactory).buildScheduledTaskEntity(dummyDto);
        verify(scheduledTaskRepository).findByName("new");
        verify(cronExpressionService).findExpression("* * * * * */12");
        verify(scheduledTaskRepository).save(dummy);
        verify(scheduledTaskDTOFactory).buildScheduledTaskDTO(dummy);
        verify(scheduledTaskInputService).createInputs(dummy, List.of(inputDummyDto));
        assertEquals(dummyDto, result);
    }
}
