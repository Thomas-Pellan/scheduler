package fr.pellan.scheduler.controller;

import fr.pellan.scheduler.service.CronExpressionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller exposin data and functionnalities for cron expressions.
 */
@Tag(name = "Cron Expression")
@Slf4j
@RestController
@RequestMapping("expression")
public class CronExpressionController {

    @Autowired
    private CronExpressionService cronExpressionService;

    /**
     * Validates a cron expression string using spring validation.
     * @param expression the expression to validate
     * @return true if valid, false otherwise
     */
    @Operation(summary = "Cron expression validation",
            description = "Validates a string as a valid Spring cron expressiob")
    @PostMapping(path="/validate")
    @ApiResponse(responseCode = "200", description = "expression accepted, see body for response")
    @ApiResponse(responseCode = "400", description = "empty expression parameter")
    public ResponseEntity<Boolean> updateTask(@RequestParam(name = "expression") String expression){

        if(StringUtils.isBlank(expression)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(cronExpressionService.validate(expression), HttpStatus.OK);
    }
}
