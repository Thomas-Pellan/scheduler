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
 * Factory used to create task input entity from dtos.
 */
@Slf4j
@Service
public class ScheduledTaskInputEntityFactory {


    public List<ScheduledTaskInputEntity> buildScheduledTaskInputEntity(List<ScheduledTaskInputDTO> dtos){

        if(CollectionUtils.isEmpty(dtos)){
            return new ArrayList<>();
        }

        return dtos.stream().map(d -> buildScheduledTaskInputEntity(d)).collect(Collectors.toList());
    }

    public ScheduledTaskInputEntity buildScheduledTaskInputEntity(ScheduledTaskInputDTO dto){

        if(dto == null){
            return null;
        }

        ScheduledTaskInputEntity entity = ScheduledTaskInputEntity.builder()
                .key(dto.getKey())
                .value(dto.getValue())
                .build();

        return entity;
    }
}
