package fr.pellan.scheduler.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HeathController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenCall_shouldRespondOk() throws Exception {

        MvcResult result = mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("UP", result.getResponse().getContentAsString());
    }
}
