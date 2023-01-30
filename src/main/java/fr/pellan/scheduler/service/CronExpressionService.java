package fr.pellan.scheduler.service;

import fr.pellan.scheduler.entity.CronExpressionEntity;
import fr.pellan.scheduler.factory.CronExceptionEntityFactory;
import fr.pellan.scheduler.repository.CronExpressionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

/**
 * Service used to manipulate cron expressions.
 */
@Slf4j
@Service
public class CronExpressionService {

    @Autowired
    CronExceptionEntityFactory cronExceptionEntityFactory;

    @Autowired
    private CronExpressionRepository cronExpressionRepository;

    /**
     * Finds existing cron expressions from the given string.
     * @param expression a string to look after
     * @return an entity saved in the db
     */
    public CronExpressionEntity findExpression(String expression){

        return cronExpressionRepository.findByExpression(expression);
    }

    /**
     * Creates a cron expression from the given string and persists it in the database.
     * @param expression the target string to persists
     * @return an entity saved in the db
     */
    public CronExpressionEntity createExpression(String expression){

        CronExpressionEntity cronExpression = cronExpressionRepository.findByExpression(expression);
        if(cronExpression == null){
            cronExpression = cronExceptionEntityFactory.buildCronExpressionEntity(expression);
            cronExpression = cronExpressionRepository.save(cronExpression);
        }

        return cronExpression;
    }

    /**
     * Validates a cron string expression
     * @param expression the string to validate
     * @return true is valid, false otherwise
     */
    public boolean validate(String expression){

        try{
            CronExpression.parse(expression);
        } catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }
}
