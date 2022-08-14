package fr.pellan.scheduler.factory;

import fr.pellan.scheduler.dto.ScheduledTaskDTO;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Factory building task dto from entities.
 */
@Slf4j
@Service
public class ScheduledTaskDTOFactory {

    public List<ScheduledTaskDTO> buildScheduledTaskDTO(List<ScheduledTaskEntity> entities){

        if(CollectionUtils.isEmpty(entities)){
            return new ArrayList<>();
        }

        return entities.stream().map(this::buildScheduledTaskDTO).collect(Collectors.toList());
    }

    public ScheduledTaskDTO buildScheduledTaskDTO(ScheduledTaskEntity entity){

        if(entity == null){
            return null;
        }

        return ScheduledTaskDTO.builder()
                .id(entity.getId())
                .active(entity.isActive())
                .name(entity.getName())
                .url(entity.getUrl())
                .lastExecution(entity.getLastExecution())
                .lastResult(entity.getLastResult())
                .cronExpression(entity.getCronExpression().getCronPattern())
                .build();
    }
}
