package fr.pellan.scheduler.task;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.service.ScheduledTaskInputService;
import fr.pellan.scheduler.service.ScheduledTaskOutputService;
import fr.pellan.scheduler.service.ScheduledTaskService;
import fr.pellan.scheduler.util.HttpUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
public class RunnableTask implements Runnable{

    private HttpUtil httpUtil;

    private ScheduledTaskEntity taskData;

    private ScheduledTaskInputService scheduledTaskInputService;

    private ScheduledTaskService scheduledTaskService;

    private ScheduledTaskOutputService scheduledTaskOutputService;

    @Override
    public void run() {

        scheduledTaskOutputService.create(taskData, TaskState.STARTED);

        //Building body data
        JsonObject body = scheduledTaskInputService.buildJsonBodyData(taskData.getInputs());

        //Send request and handle response
        HttpResponse response = httpUtil.sendHttpPost(taskData.getUrl(), body == null ? "" : body.toString());

        taskData.setLastExecution(LocalDateTime.now());
        if(response == null){
            scheduledTaskOutputService.create(taskData, TaskState.NETWORK_ERROR, null, null);
            taskData.setLastResult(null);
            scheduledTaskService.save(taskData);
            return;
        }

        taskData.setLastResult(String.valueOf(response.getStatusLine().getStatusCode()));
        scheduledTaskService.save(taskData);

        if(response.getStatusLine().getStatusCode() != 200){
            scheduledTaskOutputService.create(taskData, TaskState.INVALID_RESULT, response.getEntity().toString(), null);
            return;
        }

        if(response.getEntity() == null){
            scheduledTaskOutputService.create(taskData, TaskState.SUCCESS);
            return;
        }

        try {
            String responseContent = EntityUtils.toString(response.getEntity(), Charset.defaultCharset());
            TaskResultResponse responseDto = new Gson().fromJson(responseContent, TaskResultResponse.class);

            if(responseDto == null){
                scheduledTaskOutputService.create(taskData, TaskState.INVALID_RESULT, null, responseContent);
                return;
            }

            TaskState state;
            if(responseDto.isSuccess()){
                state = TaskState.SUCCESS;
            } else {
                state = TaskState.ERROR;
            }
            scheduledTaskOutputService.create(taskData, state, responseDto.getData(), responseDto.getError());

        } catch (IOException e) {
            scheduledTaskOutputService.create(taskData, TaskState.ERROR, null, e.getMessage());
            log.error("run : error while parsing http response content", e);
        }
    }
}
