package fr.pellan.scheduler.factory;

import fr.pellan.scheduler.dto.ScheduledTaskOutputDTO;
import fr.pellan.scheduler.entity.ScheduledTaskOutputEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScheduledTaskOutputDTOFactory {

    public List<ScheduledTaskOutputDTO> buildScheduledTaskOutputDTO(List<ScheduledTaskOutputEntity> entities){

        if(CollectionUtils.isEmpty(entities)){
            return new ArrayList<>();
        }

        return entities.stream().map(d -> buildScheduledTaskOutputDTO(d)).collect(Collectors.toList());
    }

    public ScheduledTaskOutputDTO buildScheduledTaskOutputDTO(ScheduledTaskOutputEntity entity){

        if(entity == null){
            return null;
        }

        ScheduledTaskOutputDTO dto = ScheduledTaskOutputDTO.builder()
                .data(entity.getData())
                .log(entity.getLog())
                .state(entity.getState())
                .build();

        return dto;
    }
}
