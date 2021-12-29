package fr.pellan.scheduler.controller;

import fr.pellan.scheduler.dto.ScheduledTaskDTO;
import fr.pellan.scheduler.service.ScheduledTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("scheduled-task")
public class ScheduledTaskController {

    @Autowired
    ScheduledTaskService scheduledTaskService;

    @GetMapping(path="/find/all")
    private ResponseEntity<List<ScheduledTaskDTO>> findAllTasks(){

        return new ResponseEntity<>(scheduledTaskService.find(), HttpStatus.OK);
    }

    @PostMapping(path="/modify")
    private ResponseEntity<ScheduledTaskDTO> updateTask(@RequestBody ScheduledTaskDTO taskDto){

        if(taskDto == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ScheduledTaskDTO dto = scheduledTaskService.updateTask(taskDto);
        if(dto == null){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(path="/create")
    private ResponseEntity<ScheduledTaskDTO> createTask(@RequestBody ScheduledTaskDTO taskDto){

        if(taskDto == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ScheduledTaskDTO dto = scheduledTaskService.createTask(taskDto);
        if(dto == null){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping(path="/delete")
    private ResponseEntity deleteTask(@RequestParam(name = "name") String name){

        if(StringUtils.isBlank(name)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(scheduledTaskService.deleteTask(name), HttpStatus.OK);
    }
}
