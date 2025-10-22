package com.project.rest_api.service;

import org.springframework.stereotype.Service;
import com.project.rest_api.dto.PersonDto;
import com.project.rest_api.entity.Person;
import com.project.rest_api.mapper.PersonMapper;
import com.project.rest_api.repository.PersonRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonMapper personMapper;
    private final PersonRepository personRepository;

    public PersonDto createPerson(PersonDto personDto) {
        // Check for duplicate ID
        if (personDto.getId() != null && personRepository.existsById(personDto.getId())) {
            throw new IllegalArgumentException(
                    "Person with ID " + personDto.getId() + " already exists");
        }

        Person person = personMapper.toEntity(personDto);
        Person savedPerson = personRepository.save(person);

        return personMapper.toDto(savedPerson);
    }
}
