package com.project.rest_api.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.project.rest_api.entity.Car;
import com.project.rest_api.entity.Person;

@Getter
@Setter
@JsonPropertyOrder({"id", "name", "birthdate", "cars"})
public class PersonWithCarsDto {
    private Long id;
    private String name;
    private LocalDate birthdate;
    private List<Car> cars;

    public PersonWithCarsDto(Person person, List<Car> cars) {
        this.id = person.getId();
        this.name = person.getName();
        this.birthdate = person.getBirthdate();
        this.cars = cars;
    }
}
