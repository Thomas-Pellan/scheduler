package fr.pellan.scheduler.controller;

import com.google.gson.Gson;
import fr.pellan.scheduler.dto.MockTestTaskControllerDTO;
import fr.pellan.scheduler.task.TaskResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * Prints the data received and sends back a simple response.
     * @param data the data input
     * @return a string output
     */
    @Operation(summary = "Hello World",
            description = "Prints a log with parameter and sends back somme info")
    @PostMapping(path="/helloworld")
    @ApiResponse(responseCode = "200", description = "call succeeded, see body for response")
    public ResponseEntity<String> helloWorld(@RequestBody MockTestTaskControllerDTO data){

        TaskResultResponse response = TaskResultResponse.builder()
            .success(true)
            .data(data.getTestData())
            .error("Nothing serious was broken")
            .build();

        return new ResponseEntity<>(new Gson().toJson(response), HttpStatus.OK);
    }

    /**
     * Responds with bad request, always
     *
     * @return a string output
     */
    @Operation(summary = "Bye World",
            description = "Tells you your request is bad, every time")
    @PostMapping(path="/byeworld")
    @ApiResponse(responseCode = "400'", description = "request was bad, sad")
    public ResponseEntity<String> byeWorld(){

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
