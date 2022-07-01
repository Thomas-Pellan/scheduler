package fr.pellan.scheduler.controller;

import com.google.gson.Gson;
import fr.pellan.scheduler.dto.MockTestTaskControllerDTO;
import fr.pellan.scheduler.entity.ScheduledTaskOutputEntity;
import fr.pellan.scheduler.service.ScheduledTaskOutputService;
import fr.pellan.scheduler.task.TaskResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Mock controller used to simulate a remote task execution and it's return value.
 */
@Tag(name = "Mock Controller for task response")
@Slf4j
@RestController
@RequestMapping("test")
public class MockTestTaskController {

    private static final double RANDOM_FAIL = 0.3;

    /**
     * Prints the data received and sends back a simple response.
     * @param data the data input
     * @return a string output
     */
    @Operation(summary = "Hello World",
            description = "Prints a log with parameter and sends back somme info")
    @PostMapping(path="/helloworld")
    @ApiResponse(responseCode = "200", description = "call succeeded, see body for response")
    private ResponseEntity<String> helloWorld(@RequestBody MockTestTaskControllerDTO data){

        if(Math.random() < RANDOM_FAIL){

            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        TaskResultResponse response = TaskResultResponse.builder()
            .success(true)
            .data(data.getTestData())
            .error("Nothing serious was broken")
            .build();

        return new ResponseEntity(new Gson().toJson(response), HttpStatus.OK);
    }
}
