package fr.pellan.scheduler.controller;

import fr.pellan.scheduler.service.CronExpressionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("expresion")
public class CronExpressionController {

    @Autowired
    private CronExpressionService cronExpressionService;

    @PostMapping(path="/validate")
    private ResponseEntity<Boolean> updateTask(@RequestParam(name = "expression") String expression){

        if(StringUtils.isBlank(expression)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(cronExpressionService.validate(expression), HttpStatus.OK);
    }
}
