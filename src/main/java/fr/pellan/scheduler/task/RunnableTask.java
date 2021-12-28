package fr.pellan.scheduler.task;

import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.repository.ScheduledTaskRepository;
import fr.pellan.scheduler.service.ScheduledTaskInputService;
import fr.pellan.scheduler.util.HttpUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
public class RunnableTask implements Runnable{

    private HttpUtil httpUtil;

    private ScheduledTaskEntity taskData;

    private ScheduledTaskInputService scheduledTaskInputService;

    private ScheduledTaskRepository scheduledTaskRepository;

    @Override
    public void run() {

        //Building body data
        JSONObject body = scheduledTaskInputService.buildJsonBodyData(taskData.getInputs());

        //Send request and handle response
        HttpResponse response = httpUtil.sendHttpPost(taskData.getUrl(), body.toString());

        taskData.setLastExecution(LocalDateTime.now());
        if(response != null && response.getStatusLine() != null){

            taskData.setLastResult(String.valueOf(response.getStatusLine() .getStatusCode()));
        } else {
            taskData.setLastResult("No Response");
        }


        scheduledTaskRepository.save(taskData);
    }
}
