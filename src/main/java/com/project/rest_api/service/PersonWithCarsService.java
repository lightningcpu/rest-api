package com.project.rest_api.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.project.rest_api.dto.PersonWithCarsDto;
import com.project.rest_api.entity.Car;
import com.project.rest_api.entity.Person;
import com.project.rest_api.exception.ResourceNotFoundException;
import com.project.rest_api.repository.CarRepository;
import com.project.rest_api.repository.PersonRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonWithCarsService {

    private final PersonRepository personRepository;
    private final CarRepository carRepository;

    public PersonWithCarsDto getPersonWithCars(Long personId) {
        Person person = personRepository.findById(personId).orElseThrow(
                () -> new ResourceNotFoundException("Person not found with id: " + personId));

        List<Car> cars = carRepository.findByOwnerId(personId);

        return new PersonWithCarsDto(person, cars);
    }
}
