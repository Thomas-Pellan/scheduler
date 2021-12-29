package fr.pellan.scheduler.controller;

import com.google.gson.Gson;
import fr.pellan.scheduler.dto.MockTestTaskControllerDTO;
import fr.pellan.scheduler.task.TaskResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("test")
public class MockTestTaskController {

    @PostMapping(path="/helloworld")
    private ResponseEntity<String> helloWorld(@RequestBody MockTestTaskControllerDTO data){

        if(data != null && !StringUtils.isBlank(data.getTestData())){
            log.info("helloWorld : got data from scheduler : {}", data.getTestData());
        }

        TaskResultResponse response = TaskResultResponse.builder()
                .success(true)
                .data("Oh hi Mark !!")
                .error("Nothing serious was broken")
                .build();

        return new ResponseEntity(new Gson().toJson(response), HttpStatus.OK);
    }
}
