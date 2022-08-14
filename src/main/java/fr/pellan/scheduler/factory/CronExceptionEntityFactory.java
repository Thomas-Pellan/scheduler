package fr.pellan.scheduler.factory;

import fr.pellan.scheduler.entity.CronExpressionEntity;
import fr.pellan.scheduler.repository.CronExpressionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

/**
 * Factory building cron rexpression entities.
 */
@Slf4j
@Service
public class CronExceptionEntityFactory {

    @Autowired
    CronExpressionRepository cronExpressionRepository;

    public CronExpressionEntity buildCronExpressionEntity(String cronExpression){

        if(StringUtils.isBlank(cronExpression)){
            return null;
        }

        //Cron pattern validation
        try{
            CronExpression.parse(cronExpression);
        } catch (IllegalArgumentException e){
            log.error("buildCronExpressionEntity : invalid cron expression");
            return null;
        }

        return CronExpressionEntity.builder()
                .cronPattern(cronExpression)
                .build();
    }
}
