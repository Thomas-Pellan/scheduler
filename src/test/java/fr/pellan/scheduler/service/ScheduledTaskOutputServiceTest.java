package fr.pellan.scheduler.service;

import fr.pellan.scheduler.dto.ScheduledTaskOutputDTO;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.entity.ScheduledTaskOutputEntity;
import fr.pellan.scheduler.factory.ScheduledTaskOutputDTOFactory;
import fr.pellan.scheduler.repository.ScheduledTaskOutputRepository;
import fr.pellan.scheduler.task.TaskState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduledTaskOutputServiceTest {

    @InjectMocks
    ScheduledTaskOutputService scheduledTaskOutputService;

    @Mock
    ScheduledTaskOutputRepository scheduledTaskOutputRepository;

    @Mock
    ScheduledTaskOutputDTOFactory scheduledTaskOutputDTOFactory;

    @Test
    void givenTask_whenCreateCalled_SaveOutput() {

        ScheduledTaskEntity dummyTask = mock(ScheduledTaskEntity.class);
        ScheduledTaskOutputEntity dummyOutput = new ScheduledTaskOutputEntity(1, dummyTask, null, "testData", TaskState.SUCCESS.name(), "testLog");

        when(scheduledTaskOutputRepository.save(Mockito.any(ScheduledTaskOutputEntity.class))).thenReturn(dummyOutput);

        ScheduledTaskOutputEntity output = scheduledTaskOutputService.create(dummyTask, TaskState.SUCCESS, "testData", "testLog");

        assertEquals("testData", output.getData());
        assertEquals("testLog", output.getLog());
        assertEquals(TaskState.SUCCESS.name(), output.getState());
        assertEquals(dummyTask, output.getScheduledTask());
    }

    @Test
    void givenTaskName_whenSearchForOutputs_returnOuputDtoList() {

        ScheduledTaskEntity dummyTask1 = new ScheduledTaskEntity();
        dummyTask1.setName("testName1");
        ScheduledTaskEntity dummyTask2 = new ScheduledTaskEntity();
        dummyTask2.setName("testName2");
        ScheduledTaskOutputEntity dummyOutput2 = new ScheduledTaskOutputEntity(2, dummyTask2, null, "testData2", TaskState.ERROR.name(), "testLog2");
        List<ScheduledTaskOutputEntity> dummies = List.of(new ScheduledTaskOutputEntity(1, dummyTask1, null, "testData1", TaskState.SUCCESS.name(), "testLog1"),
                dummyOutput2);
        List<ScheduledTaskOutputDTO> dummiesDto = List.of(new ScheduledTaskOutputDTO(null, "testData2", TaskState.ERROR.name(), "testLog2"));

        when(scheduledTaskOutputRepository.findByTaskName(Mockito.any(String.class))).then((Answer<List<ScheduledTaskOutputEntity>>) invocation -> {
            String query = invocation.getArgument(0);
            return dummies.stream().filter(d -> d.getScheduledTask().getName().equals(query)).collect(Collectors.toList());
        });
        when(scheduledTaskOutputDTOFactory.buildScheduledTaskOutputDTO(Mockito.any(List.class))).thenReturn(dummiesDto);

        List<ScheduledTaskOutputDTO> results = scheduledTaskOutputService.listTaskOutputs("testName2");
        verify(scheduledTaskOutputRepository).findByTaskName("testName2");
        verify(scheduledTaskOutputDTOFactory).buildScheduledTaskOutputDTO(List.of(dummyOutput2));
        assertEquals(dummiesDto, results);
    }

    @Test
    void givenTaskNameAndDate_whenPastOuputNotFound_doNothing() {

        ScheduledTaskEntity dummyTask = new ScheduledTaskEntity();
        dummyTask.setName("testName");
        ScheduledTaskOutputEntity dummyOutputFuture = new ScheduledTaskOutputEntity(1, dummyTask, LocalDateTime.now().plusDays(1), "testData", TaskState.SUCCESS.name(), "testLog");

        when(scheduledTaskOutputRepository.findByTaskName(Mockito.any(String.class))).then((Answer<List<ScheduledTaskOutputEntity>>) invocation -> {
            String query = invocation.getArgument(0);
            return Stream.of(dummyOutputFuture).filter(d -> d.getScheduledTask().getName().equals(query)).collect(Collectors.toList());
        });

        scheduledTaskOutputService.deleteBeforeDateTimerByTaskName("testName", LocalDateTime.now());
        verify(scheduledTaskOutputRepository).findByTaskName("testName");
    }

    @Test
    void givenTaskNameAndDate_deleteCorrespondingOutputs() {

        ScheduledTaskEntity dummyTask = new ScheduledTaskEntity();
        dummyTask.setName("testName");
        ScheduledTaskOutputEntity dummyOutputPast = new ScheduledTaskOutputEntity(1, dummyTask, LocalDateTime.now().minusDays(1), "testData", TaskState.SUCCESS.name(), "testLog");
        ScheduledTaskOutputEntity dummyOutputFuture = new ScheduledTaskOutputEntity(2, dummyTask, LocalDateTime.now().plusDays(1), "testData", TaskState.SUCCESS.name(), "testLog");

        when(scheduledTaskOutputRepository.findByTaskName(Mockito.any(String.class))).then((Answer<List<ScheduledTaskOutputEntity>>) invocation -> {
            String query = invocation.getArgument(0);
            return Stream.of(dummyOutputPast, dummyOutputFuture).filter(d -> d.getScheduledTask().getName().equals(query)).collect(Collectors.toList());
        });

        scheduledTaskOutputService.deleteBeforeDateTimerByTaskName("testName", LocalDateTime.now());

        verify(scheduledTaskOutputRepository).findByTaskName("testName");
        verify(scheduledTaskOutputRepository, times(1)).deleteAll(List.of(dummyOutputPast));
    }

    @Test
    void givenTask_whenOuputsNotFound_doNothing() {

        ScheduledTaskEntity dummyTask = new ScheduledTaskEntity();
        dummyTask.setName("testName");
        ScheduledTaskOutputEntity dummyOutput = new ScheduledTaskOutputEntity(1, mock(ScheduledTaskEntity.class), null, "testData", TaskState.SUCCESS.name(), "testLog");

        when(scheduledTaskOutputRepository.findByTask(Mockito.any(ScheduledTaskEntity.class))).then((Answer<List<ScheduledTaskOutputEntity>>) invocation -> {
            ScheduledTaskEntity query = invocation.getArgument(0);
            return Stream.of(dummyOutput).filter(d -> d.getScheduledTask().equals(query)).collect(Collectors.toList());
        });

        scheduledTaskOutputService.delete(dummyTask);
        verify(scheduledTaskOutputRepository).findByTask(dummyTask);
    }
    @Test
    void givenTask_whenOuputsFound_deleteThem() {

        ScheduledTaskEntity dummyTask = new ScheduledTaskEntity();
        dummyTask.setName("testName");
        ScheduledTaskOutputEntity dummyOutput = new ScheduledTaskOutputEntity(1, dummyTask, null, "testData", TaskState.SUCCESS.name(), "testLog");

        when(scheduledTaskOutputRepository.findByTask(Mockito.any(ScheduledTaskEntity.class))).then((Answer<List<ScheduledTaskOutputEntity>>) invocation -> {
            ScheduledTaskEntity query = invocation.getArgument(0);
            return Stream.of(dummyOutput).filter(d -> d.getScheduledTask().equals(query)).collect(Collectors.toList());
        });

        scheduledTaskOutputService.delete(dummyTask);
        verify(scheduledTaskOutputRepository).findByTask(dummyTask);
        verify(scheduledTaskOutputRepository, times(1)).deleteAll(List.of(dummyOutput));
    }
}
