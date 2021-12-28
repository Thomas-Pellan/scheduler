package fr.pellan.scheduler.controller;

import fr.pellan.scheduler.dto.MockTestTaskControllerDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
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
            log.info("helloWorld : look what I got : {}", data.getTestData());
        }

        JSONObject returnValue = new JSONObject();
        try {
            returnValue.put("data", "Oh hi Mark !");
        } catch (JSONException e) {
            log.error("helloWorld : error happened", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(returnValue.toString(), HttpStatus.OK);
    }
}
