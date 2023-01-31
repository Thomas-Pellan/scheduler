package fr.pellan.scheduler.service;

import fr.pellan.scheduler.dto.ScheduledTaskDTO;
import fr.pellan.scheduler.entity.CronExpressionEntity;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.factory.ScheduledTaskDTOFactory;
import fr.pellan.scheduler.factory.ScheduledTaskEntityFactory;
import fr.pellan.scheduler.repository.CronExpressionRepository;
import fr.pellan.scheduler.repository.ScheduledTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    CronExpressionService cronExpressionService;

    @Autowired
    ThreadPoolService threadPoolService;

    public List<ScheduledTaskDTO> searchTasks(String name, String url){

        return scheduledTaskDTOFactory.buildScheduledTaskDTO(scheduledTaskRepository.findByNameOrUrl(name, url));
    }

    public ScheduledTaskDTO findById(Integer id){

        return scheduledTaskDTOFactory.buildScheduledTaskDTO(scheduledTaskRepository.findById(id).orElse(null));
    }

    public List<ScheduledTaskDTO> find(){

        return scheduledTaskDTOFactory.buildScheduledTaskDTO((List<ScheduledTaskEntity>) scheduledTaskRepository.findAll());
    }

    @Transactional
    public boolean deleteTask(Integer id){

        ScheduledTaskEntity task = scheduledTaskRepository.findById(id).orElse(null);

        if(task == null){
            return false;
        }

        //Inactivate and reload the pool if task to delete was active
        if(task.isActive()){
            task.setActive(false);
            task = scheduledTaskRepository.save(task);
            threadPoolService.reloadThreadTasks();
        }

        scheduledTaskRepository.delete(task);

        return true;
    }

    @Transactional
    public ScheduledTaskDTO updateTask(ScheduledTaskDTO taskDto){

        ScheduledTaskEntity task = scheduledTaskRepository.findById(taskDto.getId()).orElse(null);
        if(task == null){
            return null;
        }

        //Update Cron if changed or create a new one
        if(StringUtils.isBlank(taskDto.getCronExpression())){
            return null;
        }

        CronExpressionEntity expression = cronExpressionService.findExpression(taskDto.getCronExpression());
        task.setCronExpression(expression != null ? expression : cronExpressionService.createExpression(taskDto.getCronExpression()));

        //Update task
        task.setActive(taskDto.isActive());
        task.setName(taskDto.getName());
        task.setUrl(taskDto.getUrl());

        ScheduledTaskEntity savedTask = scheduledTaskRepository.save(task);

        //Reload the Thread Pool if the class is active
        if(taskDto.isActive()){
            threadPoolService.reloadThreadTasks();
        }

        return scheduledTaskDTOFactory.buildScheduledTaskDTO(savedTask);
    }

    public ScheduledTaskDTO createTask(ScheduledTaskDTO taskDto){

        ScheduledTaskEntity task = scheduledTaskEntityFactory.buildScheduledTaskEntity(taskDto);
        if(task == null || StringUtils.isBlank(task.getName()) || StringUtils.isBlank(taskDto.getCronExpression())){
            return null;
        }

        //Check for existing tasks with the same data
        List<ScheduledTaskEntity> existing = scheduledTaskRepository.findByName(taskDto.getName());
        if(!CollectionUtils.isEmpty(existing)){
            return null;
        }

        //Create cron expression if it does not exist
        CronExpressionEntity expression = cronExpressionService.findExpression(taskDto.getCronExpression());
        task.setCronExpression(expression != null ? expression : cronExpressionService.createExpression(taskDto.getCronExpression()));

        ScheduledTaskEntity newTask = scheduledTaskRepository.save(task);
        ScheduledTaskDTO dto = scheduledTaskDTOFactory.buildScheduledTaskDTO(newTask);

        if(!CollectionUtils.isEmpty(taskDto.getInputs())){
            dto.setInputs(scheduledTaskInputService.createInputs(newTask, taskDto.getInputs()));
        }

        return dto;
    }
}
