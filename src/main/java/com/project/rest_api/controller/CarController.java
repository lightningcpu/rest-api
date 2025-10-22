package com.project.rest_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.rest_api.dto.CarDto;
import com.project.rest_api.service.CarService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/car")
@AllArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    public ResponseEntity<CarDto> createCar(@RequestBody @Valid CarDto carDto) {
        CarDto saveCarDto = carService.createCar(carDto);

        return new ResponseEntity<>(saveCarDto, HttpStatus.OK);
    }
}
