package fr.pellan.scheduler.controller;

import fr.pellan.scheduler.service.ThreadPoolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ThreadPoolController.class)
class ThreadPoolControllerTest {

    @MockBean
    private ThreadPoolService threadPoolService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenCall_thenRespondWithOk() throws Exception {

        mockMvc.perform(post("/pool/reload"))
                .andExpect(status().isOk());

        verify(threadPoolService).purgeThreadTasks();
        //Counting the call at start of the application
        verify(threadPoolService, times(2)).initThreadTasks();
    }
}
