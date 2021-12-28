package fr.pellan.scheduler.service;

import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.entity.ScheduledTaskInputEntity;
import fr.pellan.scheduler.repository.ScheduledTaskInputRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class ScheduledTaskInputService {

    @Autowired
    private ScheduledTaskInputRepository scheduledTaskInputRepository;

    @Transactional
    public boolean delete(ScheduledTaskEntity task){

        List<ScheduledTaskInputEntity> inputs = scheduledTaskInputRepository.findByTask(task);
        if(!CollectionUtils.isEmpty(inputs)){
            scheduledTaskInputRepository.deleteAll();
        }

        return true;
    }

    public JSONObject buildJsonBodyData(List<ScheduledTaskInputEntity> inputs){
        if(CollectionUtils.isEmpty(inputs)){
            return null;
        }

        JSONObject json = new JSONObject();
        inputs.forEach(in -> {
            try {
                json.put(in.getKey(), in.getValue());
            } catch (JSONException e) {
                log.error("buildJsonBodyData : error in json body init, data will be missing", e);
            }
        });
        return json;
    }
}
