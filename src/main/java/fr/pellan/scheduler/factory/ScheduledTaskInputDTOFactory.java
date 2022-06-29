package fr.pellan.scheduler.factory;

import fr.pellan.scheduler.dto.ScheduledTaskInputDTO;
import fr.pellan.scheduler.entity.ScheduledTaskInputEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Factory used to build task input dto from db entities.
 */
@Slf4j
@Service
public class ScheduledTaskInputDTOFactory {

    public List<ScheduledTaskInputDTO> buildScheduledTaskInputDTO(List<ScheduledTaskInputEntity> entities){

        if(CollectionUtils.isEmpty(entities)){
            return new ArrayList<>();
        }

        return entities.stream().map(d -> buildScheduledTaskInputDTO(d)).collect(Collectors.toList());
    }

    public ScheduledTaskInputDTO buildScheduledTaskInputDTO(ScheduledTaskInputEntity entity){

        if(entity == null){
            return null;
        }

        ScheduledTaskInputDTO dto = ScheduledTaskInputDTO.builder()
                .key(entity.getKey())
                .value(entity.getValue())
                .build();

        return dto;
    }
}
