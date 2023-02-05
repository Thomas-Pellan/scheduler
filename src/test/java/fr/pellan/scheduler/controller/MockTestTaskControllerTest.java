package fr.pellan.scheduler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pellan.scheduler.dto.MockTestTaskControllerDTO;
import fr.pellan.scheduler.task.TaskResultResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MockTestTaskController.class)
class MockTestTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenHelloWorld_thenRespondWithData() throws Exception {

        String input = "testData";

        MockTestTaskControllerDTO dto = new MockTestTaskControllerDTO();
        dto.setTestData(input);

        TaskResultResponse response = TaskResultResponse.builder()
                .success(true)
                .data(input)
                .error("Nothing serious was broken")
                .build();

        mockMvc.perform(post("/test/helloworld").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(response.getData()))
        .andExpect(jsonPath("$.error").value(response.getError()));
    }

    @Test
    void givenRequestBye_thenRespondWithFail() throws Exception {

        mockMvc.perform(post("/test/byeworld"))
                .andExpect(status().isBadRequest());
    }
}
