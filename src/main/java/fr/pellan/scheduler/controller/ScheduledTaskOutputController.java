package fr.pellan.scheduler.controller;

import fr.pellan.scheduler.dto.ScheduledTaskOutputDTO;
import fr.pellan.scheduler.service.ScheduledTaskOutputService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Scheduled Task Output")
@Slf4j
@RestController
@RequestMapping("scheduled-task-output")
public class ScheduledTaskOutputController {

    @Autowired
    ScheduledTaskOutputService scheduledTaskOutputService;

    @Operation(summary = "List outputs by task name",
            description = "Gets all outputs for the target task")
    @GetMapping(path="/list")
    @ApiResponse(responseCode = "200", description = "search succeeded, see body for response")
    private ResponseEntity<List<ScheduledTaskOutputDTO>> findTasks(@RequestParam(name = "taskName") String taskName){

        return new ResponseEntity<>(scheduledTaskOutputService.listTaskOutputs(taskName), HttpStatus.OK);
    }

    @Operation(summary = "Task output flush",
            description = "Deletes all outputs from the target task before the given date")
    @DeleteMapping(path="/flush")
    @ApiResponse(responseCode = "200", description = "delete succeeded")
    private ResponseEntity deleteTask(@RequestParam(name = "name") String name,
                                      @RequestParam(name = "date") LocalDateTime date){

        if(StringUtils.isBlank(name) || date == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(scheduledTaskOutputService.deleteBeforeDateTimerByTaskName(name, date), HttpStatus.OK);
    }
}
