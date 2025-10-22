package com.project.rest_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatisticsDto {
    private Long personcount;
    private Long carcount;
    private Long uniquevendorcount;
}
