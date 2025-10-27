package com.project.rest_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.rest_api.dto.StatisticsDto;
import com.project.rest_api.service.StatisticsService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/statistics")
@AllArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<StatisticsDto> getStatistics() {
        StatisticsDto saveStatisticsDto = statisticsService.getStatistics();

        return new ResponseEntity<>(saveStatisticsDto, HttpStatus.OK);
    }
}
