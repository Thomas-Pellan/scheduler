package fr.pellan.scheduler.factory;

import fr.pellan.scheduler.dto.ScheduledTaskDTO;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Factory used to build task entity from a dto.
 */
@Slf4j
@Service
public class ScheduledTaskEntityFactory {

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
