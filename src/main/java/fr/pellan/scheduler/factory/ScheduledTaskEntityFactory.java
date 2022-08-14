package fr.pellan.scheduler.factory;

import fr.pellan.scheduler.dto.ScheduledTaskDTO;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.repository.CronExpressionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Factory used to build task entity from a dto.
 */
@Slf4j
@Service
public class ScheduledTaskEntityFactory {

    @Autowired
    CronExpressionRepository cronExpressionRepository;

    public ScheduledTaskEntity buildScheduledTaskEntity(ScheduledTaskDTO dto){

        if(dto == null){
            return null;
        }

        return ScheduledTaskEntity.builder()
                .active(dto.isActive())
                .name(dto.getName())
                .url(dto.getUrl())
                .build();
    }
}
