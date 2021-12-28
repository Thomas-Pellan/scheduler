package fr.pellan.scheduler.service;

import fr.pellan.scheduler.dto.ScheduledTaskDTO;
import fr.pellan.scheduler.entity.CronExpressionEntity;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.entity.ScheduledTaskInputEntity;
import fr.pellan.scheduler.factory.CronExceptionEntityFactory;
import fr.pellan.scheduler.factory.ScheduledTaskDTOFactory;
import fr.pellan.scheduler.factory.ScheduledTaskEntityFactory;
import fr.pellan.scheduler.factory.ScheduledTaskInputEntityFactory;
import fr.pellan.scheduler.repository.CronExpressionRepository;
import fr.pellan.scheduler.repository.ScheduledTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class ScheduledTaskService {

    @Autowired
    ScheduledTaskEntityFactory scheduledTaskEntityFactory;

    @Autowired
    CronExceptionEntityFactory cronExceptionEntityFactory;

    @Autowired
    CronExpressionRepository cronExpressionRepository;

    @Autowired
    ScheduledTaskRepository scheduledTaskRepository;

    @Autowired
    ScheduledTaskDTOFactory scheduledTaskDTOFactory;

    @Autowired
    ScheduledTaskInputService scheduledTaskInputService;

    @Autowired
    ScheduledTaskOutputService scheduledTaskOutputService;

    public List<ScheduledTaskDTO> find(){

        return scheduledTaskDTOFactory.buildScheduledTaskDTO((List<ScheduledTaskEntity>) scheduledTaskRepository.findAll());
    }

    public boolean deleteTask(String name){

        List<ScheduledTaskEntity> tasks = scheduledTaskRepository.findByName(name);

        //Deleting sub entities
        tasks.forEach(t -> {
            scheduledTaskInputService.delete(t);
            scheduledTaskOutputService.delete(t);
        });

        scheduledTaskRepository.deleteAll(tasks);

        return true;
    }

    public ScheduledTaskDTO createTask(ScheduledTaskDTO taskDto){

        ScheduledTaskEntity task = scheduledTaskEntityFactory.buildScheduledTaskEntity(taskDto);
        if(task == null){
            return null;
        }

        //Create cron expression if it does not exist
        CronExpressionEntity cronExpression = cronExpressionRepository.findByExpression(taskDto.getCronExpression());
        if(cronExpression == null){
            cronExpression = cronExceptionEntityFactory.buildCronExpressionEntity(taskDto.getCronExpression());
            cronExpression = cronExpressionRepository.save(cronExpression);
        }
        task.setCronExpression(cronExpression);

        ScheduledTaskDTO dto = scheduledTaskDTOFactory.buildScheduledTaskDTO(scheduledTaskRepository.save(task));

        if(dto != null && !CollectionUtils.isEmpty(dto.getInputs())){
            dto.setInputs(scheduledTaskInputService.createInputs(dto.getInputs()));
        }

        return dto;
    }
}
