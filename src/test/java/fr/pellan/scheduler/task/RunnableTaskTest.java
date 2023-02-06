package fr.pellan.scheduler.task;

import com.google.gson.JsonObject;
import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.entity.ScheduledTaskInputEntity;
import fr.pellan.scheduler.service.ScheduledTaskInputService;
import fr.pellan.scheduler.service.ScheduledTaskOutputService;
import fr.pellan.scheduler.service.ScheduledTaskService;
import fr.pellan.scheduler.util.HttpUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunnableTaskTest {

    @InjectMocks
    RunnableTask runnableTask;

    @Mock
    HttpUtil httpUtil;

    @Mock
    ScheduledTaskEntity taskData;

    @Mock
    ScheduledTaskInputService scheduledTaskInputService;

    @Mock
    ScheduledTaskService scheduledTaskService;

    @Mock
    ScheduledTaskOutputService scheduledTaskOutputService;

    @Test
    void givenTask_whenHttpPostFails_createAndPersistAnOuputError(){

        runnableTask.run();

        verify(scheduledTaskOutputService).create(taskData, TaskState.STARTED);
        verify(scheduledTaskInputService).buildJsonBodyData(taskData.getInputs());
        verify(httpUtil).sendHttpPost(taskData.getUrl(), "");
        verify(scheduledTaskOutputService).create(taskData, TaskState.NETWORK_ERROR);
        verify(scheduledTaskService).save(taskData);
    }

    @Test
    void givenTask_whenHttpPostRespondsWithError_createAnOutputErrorWithTheResult(){

        String testUrl = "www.test.fr";
        JsonObject testBody = new JsonObject();
        testBody.addProperty("test", "test");

        ScheduledTaskEntity dummy = new ScheduledTaskEntity();
        List<ScheduledTaskInputEntity> dummyInputs = List.of(mock(ScheduledTaskInputEntity.class));
        try {
            Field privateTasksField = RunnableTask.class.getDeclaredField("taskData");
            privateTasksField.setAccessible(true);

            dummy.setUrl(testUrl);
            dummy.setInputs(dummyInputs);
            privateTasksField.set(runnableTask, dummy);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }

        HttpResponse response = mock(HttpResponse.class);
        HttpEntity responseEntity = mock(HttpEntity.class);
        when(scheduledTaskInputService.buildJsonBodyData(any(List.class))).thenReturn(testBody);
        when(response.getStatusLine()).thenReturn(mock(StatusLine.class));
        when(response.getEntity()).thenReturn(responseEntity);
        lenient().when(httpUtil.sendHttpPost(any(String.class), any(String.class))).thenReturn(response);

        runnableTask.run();

        verify(scheduledTaskOutputService).create(dummy, TaskState.STARTED);
        verify(scheduledTaskInputService).buildJsonBodyData(dummyInputs);
        verify(httpUtil).sendHttpPost(testUrl, testBody.toString());
        verify(scheduledTaskService).save(dummy);
        verify(scheduledTaskOutputService).create(dummy, TaskState.INVALID_RESULT, responseEntity.toString(), null);
    }

    @Test
    void givenTask_whenHttpPostRespondsWithOkAndEmptyEntity_createAnOutputSuccess(){

        String testUrl = "www.test.fr";
        JsonObject testBody = new JsonObject();
        testBody.addProperty("test", "test");

        ScheduledTaskEntity dummy = new ScheduledTaskEntity();
        List<ScheduledTaskInputEntity> dummyInputs = List.of(mock(ScheduledTaskInputEntity.class));
        try {
            Field privateTasksField = RunnableTask.class.getDeclaredField("taskData");
            privateTasksField.setAccessible(true);

            dummy.setUrl(testUrl);
            dummy.setInputs(dummyInputs);
            privateTasksField.set(runnableTask, dummy);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }

        HttpResponse response = mock(HttpResponse.class);
        StatusLine status = new BasicStatusLine(HttpVersion.HTTP_1_1, 200, null);
        when(scheduledTaskInputService.buildJsonBodyData(any(List.class))).thenReturn(testBody);
        when(response.getStatusLine()).thenReturn(status);
        when(response.getEntity()).thenReturn(null);
        lenient().when(httpUtil.sendHttpPost(any(String.class), any(String.class))).thenReturn(response);

        runnableTask.run();

        verify(scheduledTaskOutputService).create(dummy, TaskState.STARTED);
        verify(scheduledTaskInputService).buildJsonBodyData(dummyInputs);
        verify(httpUtil).sendHttpPost(testUrl, testBody.toString());
        verify(scheduledTaskService).save(dummy);
        verify(scheduledTaskOutputService).create(dummy, TaskState.SUCCESS);
    }

    @Test
    void givenTask_whenHttpPostRespondsWithOkAndBodyEntityInvalid_createAnOutputSuccessWithInvalidResult() {

        String testUrl = "www.test.fr";
        JsonObject testBody = new JsonObject();
        testBody.addProperty("test", "test");

        ScheduledTaskEntity dummy = new ScheduledTaskEntity();
        List<ScheduledTaskInputEntity> dummyInputs = List.of(mock(ScheduledTaskInputEntity.class));
        try {
            Field privateTasksField = RunnableTask.class.getDeclaredField("taskData");
            privateTasksField.setAccessible(true);

            dummy.setUrl(testUrl);
            dummy.setInputs(dummyInputs);
            privateTasksField.set(runnableTask, dummy);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }

        StatusLine status = new BasicStatusLine(HttpVersion.HTTP_1_1, 200, null);
        HttpResponse response = new BasicHttpResponse(status);
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(testBody.toString().getBytes()));
        response.setEntity(entity);
        when(scheduledTaskInputService.buildJsonBodyData(any(List.class))).thenReturn(testBody);
        lenient().when(httpUtil.sendHttpPost(any(String.class), any(String.class))).thenReturn(response);

        runnableTask.run();

        verify(scheduledTaskOutputService).create(dummy, TaskState.STARTED);
        verify(scheduledTaskInputService).buildJsonBodyData(dummyInputs);
        verify(httpUtil).sendHttpPost(testUrl, testBody.toString());
        verify(scheduledTaskService).save(dummy);
        verify(scheduledTaskOutputService).create(dummy, TaskState.INVALID_RESULT, null, testBody.toString());
    }

    @Test
    void givenTask_whenHttpPostRespondsWithOkAndBodyEntityValidWithErrorInside_createAnOutputSuccessWithErrorResult() {

        String testUrl = "www.test.fr";
        JsonObject testBody = new JsonObject();
        testBody.addProperty("success", "false");

        ScheduledTaskEntity dummy = new ScheduledTaskEntity();
        List<ScheduledTaskInputEntity> dummyInputs = List.of(mock(ScheduledTaskInputEntity.class));
        try {
            Field privateTasksField = RunnableTask.class.getDeclaredField("taskData");
            privateTasksField.setAccessible(true);

            dummy.setUrl(testUrl);
            dummy.setInputs(dummyInputs);
            privateTasksField.set(runnableTask, dummy);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }

        StatusLine status = new BasicStatusLine(HttpVersion.HTTP_1_1, 200, null);
        HttpResponse response = new BasicHttpResponse(status);
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(testBody.toString().getBytes()));
        response.setEntity(entity);
        when(scheduledTaskInputService.buildJsonBodyData(any(List.class))).thenReturn(testBody);
        lenient().when(httpUtil.sendHttpPost(any(String.class), any(String.class))).thenReturn(response);

        runnableTask.run();

        verify(scheduledTaskOutputService).create(dummy, TaskState.STARTED);
        verify(scheduledTaskInputService).buildJsonBodyData(dummyInputs);
        verify(httpUtil).sendHttpPost(testUrl, testBody.toString());
        verify(scheduledTaskService).save(dummy);
        verify(scheduledTaskOutputService).create(dummy, TaskState.ERROR, null, null);
    }

    @Test
    void givenTask_whenHttpPostRespondsWithOkAndBodyEntityValid_createAnOutputSuccessWithResult() {

        String testUrl = "www.test.fr";
        JsonObject testBody = new JsonObject();
        testBody.addProperty("success", "true");
        testBody.addProperty("data", "test");
        testBody.addProperty("error", "log");

        ScheduledTaskEntity dummy = new ScheduledTaskEntity();
        List<ScheduledTaskInputEntity> dummyInputs = List.of(mock(ScheduledTaskInputEntity.class));
        try {
            Field privateTasksField = RunnableTask.class.getDeclaredField("taskData");
            privateTasksField.setAccessible(true);

            dummy.setUrl(testUrl);
            dummy.setInputs(dummyInputs);
            privateTasksField.set(runnableTask, dummy);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }

        StatusLine status = new BasicStatusLine(HttpVersion.HTTP_1_1, 200, null);
        HttpResponse response = new BasicHttpResponse(status);
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(testBody.toString().getBytes()));
        response.setEntity(entity);
        when(scheduledTaskInputService.buildJsonBodyData(any(List.class))).thenReturn(testBody);
        lenient().when(httpUtil.sendHttpPost(any(String.class), any(String.class))).thenReturn(response);

        runnableTask.run();

        verify(scheduledTaskOutputService).create(dummy, TaskState.STARTED);
        verify(scheduledTaskInputService).buildJsonBodyData(dummyInputs);
        verify(httpUtil).sendHttpPost(testUrl, testBody.toString());
        verify(scheduledTaskService).save(dummy);
        verify(scheduledTaskOutputService).create(dummy, TaskState.SUCCESS, "test", "log");
    }

    @Test
    void givenTask_whenHttpPostRespondsWithOkButException_createAnOutputSuccessWithError() throws IOException {

        String testUrl = "www.test.fr";
        JsonObject testBody = new JsonObject();
        testBody.addProperty("success", "true");
        testBody.addProperty("data", "test");
        testBody.addProperty("error", "log");

        ScheduledTaskEntity dummy = new ScheduledTaskEntity();
        List<ScheduledTaskInputEntity> dummyInputs = List.of(mock(ScheduledTaskInputEntity.class));
        try {
            Field privateTasksField = RunnableTask.class.getDeclaredField("taskData");
            privateTasksField.setAccessible(true);

            dummy.setUrl(testUrl);
            dummy.setInputs(dummyInputs);
            privateTasksField.set(runnableTask, dummy);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }

        StatusLine status = new BasicStatusLine(HttpVersion.HTTP_1_1, 200, null);
        HttpResponse response = new BasicHttpResponse(status);
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(testBody.toString().getBytes()));
        response.setEntity(entity);
        when(scheduledTaskInputService.buildJsonBodyData(any(List.class))).thenReturn(testBody);
        lenient().when(httpUtil.sendHttpPost(any(String.class), any(String.class))).thenReturn(response);

        try (MockedStatic<EntityUtils> util = mockStatic(EntityUtils.class)) {
            util.when(() -> EntityUtils.toString(entity, Charset.defaultCharset())).thenThrow(new IOException("failed to parse body"));

            runnableTask.run();

            verify(scheduledTaskOutputService).create(dummy, TaskState.STARTED);
            verify(scheduledTaskInputService).buildJsonBodyData(dummyInputs);
            verify(httpUtil).sendHttpPost(testUrl, testBody.toString());
            verify(scheduledTaskService).save(dummy);
            verify(scheduledTaskOutputService).create(dummy, TaskState.ERROR, null, "failed to parse body");
        }
    }
}
