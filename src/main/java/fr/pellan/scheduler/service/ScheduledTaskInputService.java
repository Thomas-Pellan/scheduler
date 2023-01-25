package fr.pellan.scheduler.service;

import com.google.gson.JsonObject;
import fr.pellan.scheduler.dto.ScheduledTaskInputDTO;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.entity.ScheduledTaskInputEntity;
import fr.pellan.scheduler.factory.ScheduledTaskInputDTOFactory;
import fr.pellan.scheduler.factory.ScheduledTaskInputEntityFactory;
import fr.pellan.scheduler.repository.ScheduledTaskInputRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ScheduledTaskInputService {


    @Autowired
    private ScheduledTaskInputRepository scheduledTaskInputRepository;

    @Autowired
    ScheduledTaskInputEntityFactory scheduledTaskInputEntityFactory;

    @Autowired
    ScheduledTaskInputDTOFactory scheduledTaskInputDTOFactory;

    public List<ScheduledTaskInputDTO> createInputs(ScheduledTaskEntity task, List<ScheduledTaskInputDTO> dtos){

        List<ScheduledTaskInputEntity> inputs = scheduledTaskInputEntityFactory.buildScheduledTaskInputEntity(dtos);
        if(CollectionUtils.isEmpty(inputs)){
            return new ArrayList<>();
        }
        inputs.forEach(i -> i.setScheduledTask(task));
        List<ScheduledTaskInputEntity> inputsSaved = (List<ScheduledTaskInputEntity>) scheduledTaskInputRepository.saveAll(inputs);

        return scheduledTaskInputDTOFactory.buildScheduledTaskInputDTO(inputsSaved);
    }

    public boolean deleteInputs(ScheduledTaskEntity task){

        List<ScheduledTaskInputEntity> inputs = scheduledTaskInputRepository.findByTask(task);
        if(!CollectionUtils.isEmpty(inputs)){
            scheduledTaskInputRepository.deleteAll();
        }

        return true;
    }

    public JsonObject buildJsonBodyData(List<ScheduledTaskInputEntity> inputs){
        if(CollectionUtils.isEmpty(inputs)){
            return null;
        }

        JsonObject json = new JsonObject();
        inputs.forEach(in -> {
            json.addProperty(in.getKey(), in.getValue());
        });
        return json;
    }
}
