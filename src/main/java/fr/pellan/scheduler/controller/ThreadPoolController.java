package fr.pellan.scheduler.controller;

import fr.pellan.scheduler.service.ThreadPoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to manage the task thread pool
 */
@Tag(name = "Task thread pool operations")
@Slf4j
@RestController
@RequestMapping("pool")
public class ThreadPoolController {

    @Autowired
    ThreadPoolService threadPoolService;

    /**
     * Reloads thread pool with up to date tasks to execure
     * @return a http ok entity
     */
    @Operation(summary = "Reload task pool",
            description = "Destroys the current task pool tasks and creates a new one with all the active tasks in database")
    @PostMapping(path="/reload")
    @ApiResponse(responseCode = "200", description = "reload succeeded")
    public ResponseEntity<HttpStatus> reloadThreadPool(){

        threadPoolService.purgeThreadTasks();

        threadPoolService.initThreadTasks();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
