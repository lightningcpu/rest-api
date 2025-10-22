package com.project.rest_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.project.rest_api.dto.PersonWithCarsDto;
import com.project.rest_api.service.PersonWithCarsService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/personwithcars")
@AllArgsConstructor
public class PersonWithCarsController {

    private final PersonWithCarsService personWithCarsService;

    @GetMapping
    public ResponseEntity<PersonWithCarsDto> getPersonWithCars(
            @RequestParam("personid") Long personId) {
        PersonWithCarsDto dto = personWithCarsService.getPersonWithCars(personId);

        return ResponseEntity.ok(dto);
    }
}
