package fr.pellan.scheduler.service;

import fr.pellan.scheduler.dto.ScheduledTaskDTO;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.factory.ScheduledTaskDTOFactory;
import fr.pellan.scheduler.factory.ScheduledTaskEntityFactory;
import fr.pellan.scheduler.repository.ScheduledTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    ScheduledTaskRepository scheduledTaskRepository;

    @Autowired
    ScheduledTaskDTOFactory scheduledTaskDTOFactory;

    @Autowired
    ScheduledTaskInputService scheduledTaskInputService;

    @Autowired
    ScheduledTaskOutputService scheduledTaskOutputService;

    @Autowired
    CronExpressionService cronExpressionService;

    public List<ScheduledTaskDTO> find(){

        return scheduledTaskDTOFactory.buildScheduledTaskDTO((List<ScheduledTaskEntity>) scheduledTaskRepository.findAll());
    }

    public boolean deleteTask(String name){

        List<ScheduledTaskEntity> tasks = scheduledTaskRepository.findByName(name);

        //Deleting sub entities
        tasks.forEach(t -> {
            scheduledTaskInputService.deleteInputs(t);
            scheduledTaskOutputService.delete(t);
        });

        scheduledTaskRepository.deleteAll(tasks);

        return true;
    }

    public ScheduledTaskDTO updateTask(ScheduledTaskDTO taskDto){

        ScheduledTaskEntity task = scheduledTaskRepository.findById(taskDto.getId()).orElse(null);
        if(task == null){
            return null;
        }

        //Update Cron if changed or create a new one
        if(!StringUtils.isBlank(taskDto.getCronExpression())){
            task.setCronExpression(cronExpressionService.createExpression(taskDto.getCronExpression()));
        }

        //Update task
        task.setActive(taskDto.isActive());
        task.setName(taskDto.getName());
        task.setUrl(taskDto.getUrl());

        return scheduledTaskDTOFactory.buildScheduledTaskDTO(scheduledTaskRepository.save(task));
    }

    public ScheduledTaskDTO createTask(ScheduledTaskDTO taskDto){

        ScheduledTaskEntity task = scheduledTaskEntityFactory.buildScheduledTaskEntity(taskDto);
        if(task == null){
            return null;
        }

        //Check for existing tasks with the same data
        List<ScheduledTaskEntity> existing = scheduledTaskRepository.findByName(taskDto.getName());
        if(!CollectionUtils.isEmpty(existing)){
            return null;
        }

        //Create cron expression if it does not exist
        task.setCronExpression(cronExpressionService.createExpression(taskDto.getCronExpression()));

        ScheduledTaskEntity newTask = scheduledTaskRepository.save(task);
        ScheduledTaskDTO dto = scheduledTaskDTOFactory.buildScheduledTaskDTO(newTask);

        if(!CollectionUtils.isEmpty(taskDto.getInputs())){
            dto.setInputs(scheduledTaskInputService.createInputs(newTask, taskDto.getInputs()));
        }

        return dto;
    }
}
