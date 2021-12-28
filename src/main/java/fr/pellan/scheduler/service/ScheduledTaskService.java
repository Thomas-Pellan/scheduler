package fr.pellan.scheduler.service;

import fr.pellan.scheduler.dto.ScheduledTaskDTO;
import fr.pellan.scheduler.entity.CronExpressionEntity;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.factory.CronExceptionEntityFactory;
import fr.pellan.scheduler.factory.ScheduledTaskDTOFactory;
import fr.pellan.scheduler.factory.ScheduledTaskEntityFactory;
import fr.pellan.scheduler.repository.CronExpressionRepository;
import fr.pellan.scheduler.repository.ScheduledTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<ScheduledTaskDTO> find(){

        return scheduledTaskDTOFactory.buildScheduledTaskDTO((List<ScheduledTaskEntity>) scheduledTaskRepository.findAll());
    }

    public boolean deleteTask(String name){

        List<ScheduledTaskEntity> tasks = scheduledTaskRepository.findByName(name);
        scheduledTaskRepository.deleteAll(tasks);

        return true;
    }

    public ScheduledTaskDTO createTask(ScheduledTaskDTO taskDto){

        //Create cron expression if it does not exist
        CronExpressionEntity cronExpression = cronExpressionRepository.findByExpression(taskDto.getCronExpression());
        if(cronExpression == null){
            cronExpression = cronExceptionEntityFactory.buildCronExpressionEntity(taskDto.getCronExpression());
            cronExpression = cronExpressionRepository.save(cronExpression);
        }

        //Create task
        ScheduledTaskEntity task = scheduledTaskEntityFactory.buildScheduledTaskEntity(taskDto);
        task.setCronExpression(cronExpression);
        return scheduledTaskDTOFactory.buildScheduledTaskDTO(scheduledTaskRepository.save(task));
    }
}
