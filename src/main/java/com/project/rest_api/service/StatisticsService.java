package com.project.rest_api.service;

import org.springframework.stereotype.Service;
import com.project.rest_api.dto.StatisticsDto;
import com.project.rest_api.repository.CarRepository;
import com.project.rest_api.repository.PersonRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final PersonRepository personRepository;
    private final CarRepository carRepository;

    public StatisticsDto getStatistics() {
        Long personCount = personRepository.count();
        Long carCount = carRepository.count();
        Long uniqueVendorCount = carRepository.countDistinctModels();

        return new StatisticsDto(personCount, carCount, uniqueVendorCount);
    }
}
