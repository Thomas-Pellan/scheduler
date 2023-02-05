package fr.pellan.scheduler.controller;

import fr.pellan.scheduler.service.CronExpressionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CronExpressionController.class)
class CronExpressionControllerTest {

    @MockBean
    private CronExpressionService cronExpressionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenCallWithEmptyString_shouldRespondBadRequest() throws Exception {

        mockMvc.perform(post("/expression/validate").queryParam("expression", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenCallWithInvalidString_shouldRespondOkWithFalse() throws Exception {

        given(cronExpressionService.validate(any(String.class))).willReturn(false);

        MvcResult result = mockMvc.perform(post("/expression/validate").queryParam("expression", "* * * * *"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("false", result.getResponse().getContentAsString());
    }

    @Test
    void givenCallWithInvalidString_shouldRespondOkWithTrue() throws Exception {

        given(cronExpressionService.validate(any(String.class))).willReturn(true);

        MvcResult result = mockMvc.perform(post("/expression/validate").queryParam("expression", "* * * * * *"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("true", result.getResponse().getContentAsString());
    }
}
