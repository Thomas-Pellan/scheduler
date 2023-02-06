package fr.pellan.scheduler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pellan.scheduler.dto.ScheduledTaskOutputDTO;
import fr.pellan.scheduler.service.ScheduledTaskOutputService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ScheduledTaskOutputController.class)
class ScheduledTaskOutputControllerTest {

    @MockBean
    ScheduledTaskOutputService scheduledTaskOutputService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenCallWithValidString_whenListOuputs_thenRespondWithOkAndAListOfOutputsDtos() throws Exception {

        List<ScheduledTaskOutputDTO> dtos = List.of(mock(ScheduledTaskOutputDTO.class));

        given(scheduledTaskOutputService.listTaskOutputs(any(String.class))).willReturn(dtos);

        String query = "testTaskName";
        MvcResult result = mockMvc.perform(get("/scheduled-task-output/list").queryParam("taskName", query))
                .andExpect(status().isOk()).andReturn();

        verify(scheduledTaskOutputService).listTaskOutputs(query);
        assertEquals(objectMapper.writeValueAsString(dtos), result.getResponse().getContentAsString());
    }

    @Test
    void givenCallWithEmptyString_whenDeletingOuputs_thenRespondWithBadRequest() throws Exception {

       mockMvc.perform(delete("/scheduled-task-output/flush").queryParam("name", " "))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenCallWithValidNameAndDate_whenDeletingOuputs_thenRespondWithOk() throws Exception {

        LocalDateTime queryDate = LocalDateTime.now();
        String query = "testTaskName";
        given(scheduledTaskOutputService.deleteBeforeDateTimerByTaskName(any(String.class), any(LocalDateTime.class))).willReturn(true);

        MvcResult result = mockMvc.perform(delete("/scheduled-task-output/flush")
                        .queryParam("name", query)
                        .queryParam("date", queryDate.format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(status().isOk()).andReturn();

        verify(scheduledTaskOutputService).deleteBeforeDateTimerByTaskName(query, queryDate);
        assertEquals("true", result.getResponse().getContentAsString());
    }
}
