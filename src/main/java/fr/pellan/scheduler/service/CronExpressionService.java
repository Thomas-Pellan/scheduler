package fr.pellan.scheduler.service;

import fr.pellan.scheduler.entity.CronExpressionEntity;
import fr.pellan.scheduler.factory.CronExceptionEntityFactory;
import fr.pellan.scheduler.repository.CronExpressionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CronExpressionService {

    @Autowired
    CronExceptionEntityFactory cronExceptionEntityFactory;

    @Autowired
    private CronExpressionRepository cronExpressionRepository;

    public CronExpressionEntity createExpression(String expression){

        CronExpressionEntity cronExpression = cronExpressionRepository.findByExpression(expression);
        if(cronExpression == null){
            cronExpression = cronExceptionEntityFactory.buildCronExpressionEntity(expression);
            cronExpression = cronExpressionRepository.save(cronExpression);
        }

        return cronExpression;
    }

    public boolean validate(String expression){

        try{
            CronExpression.parse(expression);
        } catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }
}
