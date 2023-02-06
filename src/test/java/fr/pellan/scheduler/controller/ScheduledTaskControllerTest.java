package fr.pellan.scheduler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pellan.scheduler.dto.ScheduledTaskDTO;
import fr.pellan.scheduler.service.ScheduledTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ScheduledTaskController.class)
class ScheduledTaskControllerTest {

    @MockBean
    ScheduledTaskService scheduledTaskService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenCallWithValidStrings_whenSearchTasks_thenRespondWithOkAndAListOfDtos() throws Exception {

        List<ScheduledTaskDTO> dtos = List.of(mock(ScheduledTaskDTO.class));

        given(scheduledTaskService.searchTasks(any(String.class), any(String.class))).willReturn(dtos);

        String query = "test";
        MvcResult result = mockMvc.perform(get("/scheduled-task/find").queryParam("name", query).queryParam("url", query))
                .andExpect(status().isOk()).andReturn();

        verify(scheduledTaskService).searchTasks(query, query);
        assertEquals(objectMapper.writeValueAsString(dtos), result.getResponse().getContentAsString());
    }

    @Test
    void givenCallWithValidId_whenFindByIdTask_thenRespondWithOkAndADto() throws Exception {

        ScheduledTaskDTO dto = new ScheduledTaskDTO();
        dto.setId(1);

        given(scheduledTaskService.findById(any(Integer.class))).willReturn(dto);

        MvcResult result = mockMvc.perform(get("/scheduled-task/find/id").queryParam("id", String.valueOf(1)))
                .andExpect(status().isOk()).andReturn();

        verify(scheduledTaskService).findById(1);
        assertEquals(objectMapper.writeValueAsString(dto), result.getResponse().getContentAsString());
    }

    @Test
    void givenCall_whenFindAll_thenRespondWithOkAndAListOfDtos() throws Exception {

        List<ScheduledTaskDTO> dtos = List.of(mock(ScheduledTaskDTO.class));

        given(scheduledTaskService.find()).willReturn(dtos);

        MvcResult result = mockMvc.perform(get("/scheduled-task/find/all"))
                .andExpect(status().isOk()).andReturn();

        verify(scheduledTaskService).find();
        assertEquals(objectMapper.writeValueAsString(dtos), result.getResponse().getContentAsString());
    }

    @Test
    void givenUpdateCall_whenEmptyDto_thenRespondWithBadRequest() throws Exception {

        mockMvc.perform(post("/scheduled-task/modify").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenUpdateCall_whenDtoButUpdateFails_thenRespondWithFailedExpectations() throws Exception {

        ScheduledTaskDTO dto = new ScheduledTaskDTO();
        dto.setId(0);

        given(scheduledTaskService.updateTask(any(ScheduledTaskDTO.class))).willReturn(null);

        mockMvc.perform(post("/scheduled-task/modify").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isExpectationFailed());

        verify(scheduledTaskService).updateTask(dto);
    }

    @Test
    void givenUpdateCall_whenDtoAndUpdateSuccessfull_thenRespondWithOkAndADto() throws Exception {

        ScheduledTaskDTO dto = new ScheduledTaskDTO();
        dto.setId(0);

        given(scheduledTaskService.updateTask(any(ScheduledTaskDTO.class))).willReturn(dto);

        MvcResult result = mockMvc.perform(post("/scheduled-task/modify").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk()).andReturn();

        verify(scheduledTaskService).updateTask(dto);
        assertEquals(objectMapper.writeValueAsString(dto), result.getResponse().getContentAsString());
    }

    @Test
    void givenCreateCall_whenDtoEmpty_thenRespondWithBadRequest() throws Exception {

        ScheduledTaskDTO dto = null;

        mockMvc.perform(put("/scheduled-task/create").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenCreateCall_whenDtoValidButCreateFails_thenRespondWithExpectationsFailed() throws Exception {

        ScheduledTaskDTO dto = new ScheduledTaskDTO();
        dto.setId(0);

        given(scheduledTaskService.createTask(any(ScheduledTaskDTO.class))).willReturn(null);

        mockMvc.perform(put("/scheduled-task/create").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isExpectationFailed());

        verify(scheduledTaskService).createTask(dto);
    }

    @Test
    void givenCreateCall_whenDtoValidAndCreateSuccess_thenRespondWithOkAndADto() throws Exception {

        ScheduledTaskDTO dto = new ScheduledTaskDTO();
        dto.setId(0);

        given(scheduledTaskService.createTask(any(ScheduledTaskDTO.class))).willReturn(dto);

        MvcResult result = mockMvc.perform(put("/scheduled-task/create").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk()).andReturn();

        verify(scheduledTaskService).createTask(dto);
        assertEquals(objectMapper.writeValueAsString(dto), result.getResponse().getContentAsString());
    }

    @Test
    void givenDeleteCall_whenNullId_thenRespondWithBadRequest() throws Exception {

        mockMvc.perform(delete("/scheduled-task/delete").queryParam("id", "null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenDeleteCall_whenIdNotNull_thenRespondWithOkAndServiceResultBoolean() throws Exception {

        given(scheduledTaskService.deleteTask(any(Integer.class))).willReturn(true);

        MvcResult result = mockMvc.perform(delete("/scheduled-task/delete").queryParam("id", "1"))
                .andExpect(status().isOk()).andReturn();

        verify(scheduledTaskService).deleteTask(1);
        assertEquals("true", result.getResponse().getContentAsString());
    }
}
