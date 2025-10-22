package com.project.rest_api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.project.rest_api.dto.PersonWithCarsDto;
import com.project.rest_api.entity.Car;
import com.project.rest_api.entity.Person;
import com.project.rest_api.service.PersonWithCarsService;

@WebMvcTest(controllers = PersonWithCarsController.class)
public class PersonWithCarsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonWithCarsService personWithCarsService;

    @Test
    void getPersonWithCars_ValidPersonId_ReturnsPersonWithCars() throws Exception {
        // Arrange
        Long personId = 1L;
        Person owner = new Person(personId, "John", LocalDate.of(1990, 1, 1));
        List<Car> cars =
                Arrays.asList(new Car(1L, "BMW-35Q", 125, 5L), new Car(2L, "BMW-36Q", 135, 5L));

        PersonWithCarsDto response = new PersonWithCarsDto(owner, cars);

        when(personWithCarsService.getPersonWithCars(personId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/personwithcars").param("personid", personId.toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(personId))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.birthdate").value("1990-01-01"))
                .andExpect(jsonPath("$.cars.length()").value(2))
                .andExpect(jsonPath("$.cars[0].id").value(1L))
                .andExpect(jsonPath("$.cars[0].model").value("BMW-35Q"))
                .andExpect(jsonPath("$.cars[1].id").value(2L))
                .andExpect(jsonPath("$.cars[1].model").value("BMW-36Q"));
    }

    @Test
    void getPersonWithCars_PersonWithNoCars_ReturnsPersonWithEmptyCarList() throws Exception {
        // Arrange
        Long personId = 2L;
        Person owner = new Person(personId, "Alice", LocalDate.of(1985, 5, 15));
        List<Car> cars = Arrays.asList(); // Empty car list

        PersonWithCarsDto response = new PersonWithCarsDto(owner, cars);

        when(personWithCarsService.getPersonWithCars(personId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/personwithcars").param("personid", personId.toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(personId))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.birthdate").value("1985-05-15"))
                .andExpect(jsonPath("$.cars.length()").value(0));
    }
}
