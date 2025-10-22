package com.project.rest_api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.project.rest_api.dto.StatisticsDto;
import com.project.rest_api.service.StatisticsService;

@WebMvcTest(controllers = StatisticsController.class)
public class StatisticsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StatisticsService statisticsService;

    @Test
    void statisticsControllerTests_GetStats() throws Exception {
        StatisticsDto response = new StatisticsDto(5L, 7L, 5L);

        when(statisticsService.getStatistics()).thenReturn(response);

        mockMvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.personcount").value(5))
                .andExpect(jsonPath("$.carcount").value(7))
                .andExpect(jsonPath("$.uniquevendorcount").value(5));
    }

    @Test
    void getStatistics_NoData_ReturnsZeroStatistics() throws Exception {
        StatisticsDto response = new StatisticsDto(0L, 0L, 0L);

        when(statisticsService.getStatistics()).thenReturn(response);

        mockMvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.personcount").value(0))
                .andExpect(jsonPath("$.carcount").value(0))
                .andExpect(jsonPath("$.uniquevendorcount").value(0));
    }
}
