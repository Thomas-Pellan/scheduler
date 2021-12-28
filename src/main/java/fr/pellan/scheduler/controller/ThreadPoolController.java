package fr.pellan.scheduler.controller;

import fr.pellan.scheduler.service.ThreadPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("pool")
public class ThreadPoolController {

    @Autowired
    ThreadPoolService threadPoolService;

    @PostMapping(path="/reload")
    private ResponseEntity reloadThreadPool(){

        threadPoolService.reloadThreadTasks();

        return new ResponseEntity(HttpStatus.OK);
    }
}
