package com.project.rest_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.rest_api.repository.CarRepository;
import com.project.rest_api.repository.PersonRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClearService {
    private final PersonRepository personRepository;
    private final CarRepository carRepository;

    @Transactional
    public void clearTable() {
        carRepository.deleteAll();
        personRepository.deleteAll();
    }
}
