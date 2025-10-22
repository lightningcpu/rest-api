package com.project.rest_api.service;

import org.springframework.stereotype.Service;
import com.project.rest_api.dto.CarDto;
import com.project.rest_api.entity.Car;
import com.project.rest_api.entity.Person;
import com.project.rest_api.mapper.CarMapper;
import com.project.rest_api.repository.CarRepository;
import com.project.rest_api.repository.PersonRepository;
import lombok.AllArgsConstructor;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

@Service
@AllArgsConstructor
public class CarService {

    private final CarMapper carMapper;
    private final CarRepository carRepository;
    private final PersonRepository personRepository;
    private final Clock clock;

    public CarDto createCar(CarDto carDto) {
        // Check for duplicate Id
        if (carDto.getId() != null && carRepository.existsById(carDto.getId())) {
            throw new IllegalArgumentException("Car with ID " + carDto.getId() + " already exists");
        }

        // Check if owner even exists
        Person owner = personRepository.findById(carDto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Person with ID " + carDto.getOwnerId() + " not found"));

        // Check if owner is at least 18 years old
        if (!isAtLeast18YearsOld(owner.getBirthdate())) {
            throw new IllegalArgumentException("Owner must be at least 18 years old");
        }

        Car car = carMapper.toEntity(carDto);
        Car savedCar = carRepository.save(car);
        return carMapper.toDto(savedCar);
    }

    // helper function
    private boolean isAtLeast18YearsOld(LocalDate birthdate) {
        return Period.between(birthdate, LocalDate.now(clock)).getYears() >= 18;
    }
}
