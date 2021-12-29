package fr.pellan.scheduler.controller;

import fr.pellan.scheduler.dto.ScheduledTaskDTO;
import fr.pellan.scheduler.service.ScheduledTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Scheduled Task")
@Slf4j
@RestController
@RequestMapping("scheduled-task")
public class ScheduledTaskController {

    @Autowired
    ScheduledTaskService scheduledTaskService;

    @Operation(summary = "Find tasks",
            description = "Searches for tasks currently saved in the database using parameters")
    @GetMapping(path="/find")
    @ApiResponse(responseCode = "200", description = "search succeeded, see body for response")
    private ResponseEntity<List<ScheduledTaskDTO>> findTasks(@RequestParam(name = "name", required = false) String name,
                                                             @RequestParam(name = "url", required = false) String url){

        return new ResponseEntity<>(scheduledTaskService.searchTasks(name, url), HttpStatus.OK);
    }

    @Operation(summary = "Find all database tasks",
            description = "Searches for all tasks currently saved in the database")
    @GetMapping(path="/find/all")
    @ApiResponse(responseCode = "200", description = "search succeeded, see body for response")
    private ResponseEntity<List<ScheduledTaskDTO>> findAllTasks(){

        return new ResponseEntity<>(scheduledTaskService.find(), HttpStatus.OK);
    }

    @Operation(summary = "Task editing",
            description = "Persists new data on the target task (name, active, url, cron expression)")
    @PostMapping(path="/modify")
    @ApiResponse(responseCode = "200", description = "update succeeded, see body for response")
    @ApiResponse(responseCode = "400", description = "update body is null")
    @ApiResponse(responseCode = "417", description = "update body was not valid for task update")
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

    @Operation(summary = "Task creation",
            description = "Creates a new task with the given paraemeters, if it is active you may want to reload the thread pool so it starts.")
    @PutMapping(path="/create")
    @ApiResponse(responseCode = "200", description = "create succeeded, see body for response")
    @ApiResponse(responseCode = "400", description = "create body is null")
    @ApiResponse(responseCode = "417", description = "create body was not valid for task creation")
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

    @Operation(summary = "Task deletion",
            description = "Deletes the target task and it's info (logs, inputs, etc).")
    @DeleteMapping(path="/delete")
    @ApiResponse(responseCode = "200", description = "delete succeeded")
    @ApiResponse(responseCode = "400", description = "name parameter is null")
    private ResponseEntity deleteTask(@RequestParam(name = "name") String name){

        if(StringUtils.isBlank(name)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(scheduledTaskService.deleteTask(name), HttpStatus.OK);
    }
}
