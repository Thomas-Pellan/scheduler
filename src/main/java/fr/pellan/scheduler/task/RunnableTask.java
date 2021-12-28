package fr.pellan.scheduler.task;

import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.entity.ScheduledTaskOutputEntity;
import fr.pellan.scheduler.repository.ScheduledTaskOutputRepository;
import fr.pellan.scheduler.repository.ScheduledTaskRepository;
import fr.pellan.scheduler.service.ScheduledTaskInputService;
import fr.pellan.scheduler.util.HttpUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class RunnableTask implements Runnable{

    private HttpUtil httpUtil;

    private ScheduledTaskEntity taskData;

    private ScheduledTaskInputService scheduledTaskInputService;

    private ScheduledTaskRepository scheduledTaskRepository;

    private ScheduledTaskOutputRepository scheduledTaskOutputRepository;

    @Override
    public void run() {

        //Building body data
        JSONObject body = scheduledTaskInputService.buildJsonBodyData(taskData.getInputs());

        //Send request and handle response
        HttpResponse response = httpUtil.sendHttpPost(taskData.getUrl(), body.toString());

        taskData.setLastExecution(LocalDateTime.now());
        if(response == null){
            taskData.setLastResult("No Response");
            scheduledTaskRepository.save(taskData);
            return;
        }

        taskData.setLastResult(String.valueOf(response.getStatusLine() .getStatusCode()));
        scheduledTaskRepository.save(taskData);

        if(response.getEntity() == null){
            return;
        }

        try {
            String responseContent = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            ScheduledTaskOutputEntity taskOutput = ScheduledTaskOutputEntity.builder()
                    .executionDate(LocalDateTime.now())
                    .returnValue(responseContent)
                    .scheduledTask(taskData)
                    .build();

            scheduledTaskOutputRepository.save(taskOutput);

        } catch (IOException e) {
            log.error("run : error while parsing http response content", e);
        }
    }
}
