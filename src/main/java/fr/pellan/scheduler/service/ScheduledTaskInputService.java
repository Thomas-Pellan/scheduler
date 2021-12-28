package fr.pellan.scheduler.service;

import fr.pellan.scheduler.entity.ScheduledTaskInputEntity;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class ScheduledTaskInputService {

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
