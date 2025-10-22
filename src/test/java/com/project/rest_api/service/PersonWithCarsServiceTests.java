package com.project.rest_api.service;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.rest_api.dto.PersonWithCarsDto;
import com.project.rest_api.entity.Car;
import com.project.rest_api.entity.Person;
import com.project.rest_api.exception.ResourceNotFoundException;
import com.project.rest_api.repository.CarRepository;
import com.project.rest_api.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class PersonWithCarsServiceTests {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private PersonWithCarsService personWithCarsService;

    @Test
    void personWithCarsService_shouldReturnOwnerAndHisCars() {

        // Arrange
        Long personId = 5L;

        Person owner = new Person(personId, "John", LocalDate.of(1990, 1, 1));

        List<Car> cars =
                Arrays.asList(new Car(1L, "BMW-35Q", 125, 5L), new Car(2L, "BMW-36Q", 135, 5L));

        when(personRepository.findById(personId)).thenReturn(Optional.of(owner));
        when(carRepository.findByOwnerId(personId)).thenReturn(cars);

        // Act
        PersonWithCarsDto result = personWithCarsService.getPersonWithCars(personId);

        // Asser
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(owner.getName());
        assertThat(result.getBirthdate()).isEqualTo(owner.getBirthdate());
        assertThat(result.getCars()).hasSize(2);
        assertThat(result.getCars()).containsExactlyElementsOf(cars);

        // Verify interactions
        verify(personRepository).findById(personId);
        verify(carRepository).findByOwnerId(personId);
    }

    @Test
    void getPersonWithCars_PersonExistsNoCars_ReturnsPersonWithEmptyCarList() {
        // Arrange
        Long personId = 2L;
        Person owner = new Person(personId, "Jane Smith", LocalDate.of(1985, 5, 15));
        List<Car> emptyCarList = Collections.emptyList();

        when(personRepository.findById(personId)).thenReturn(Optional.of(owner));
        when(carRepository.findByOwnerId(personId)).thenReturn(emptyCarList);

        // Act
        PersonWithCarsDto result = personWithCarsService.getPersonWithCars(personId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(owner.getName());
        assertThat(result.getBirthdate()).isEqualTo(owner.getBirthdate());
        assertThat(result.getCars()).isEmpty();

        // Verify interactions
        verify(personRepository).findById(personId);
        verify(carRepository).findByOwnerId(personId);
    }

    @Test
    void getPersonWithCars_PersonNotFound_ThrowsResourceNotFoundException() {

        Long personId = 5L;

        when(personRepository.findById(personId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> personWithCarsService.getPersonWithCars(personId));

        assertEquals("Person not found with id: 5", ex.getMessage());
        verify(carRepository, never()).findByOwnerId(personId);
    }
}
