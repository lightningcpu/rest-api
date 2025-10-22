package com.project.rest_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.rest_api.dto.PersonDto;
import com.project.rest_api.service.PersonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@RequestBody @Valid PersonDto personDto) {
        PersonDto savPersonDto = personService.createPerson(personDto);

        return new ResponseEntity<>(savPersonDto, HttpStatus.OK);
    }
}
