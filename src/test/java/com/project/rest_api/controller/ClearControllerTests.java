package com.project.rest_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.project.rest_api.service.ClearService;

@WebMvcTest(controllers = ClearController.class)
public class ClearControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClearService clearService;

    @Test
    void clearController_shouldReturn200() throws Exception {

        clearService.clearTable();

        mockMvc.perform(delete("/clear")).andExpect(status().isOk());
    }
}
