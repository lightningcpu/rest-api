package com.project.rest_api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import com.project.rest_api.dto.CarDto;
import com.project.rest_api.entity.Car;
import com.project.rest_api.entity.Person;
import com.project.rest_api.mapper.CarMapper;
import com.project.rest_api.repository.CarRepository;
import com.project.rest_api.repository.PersonRepository;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CarServiceTests {

    @Mock
    private CarRepository carRepository;

    @Mock
    private PersonRepository personRepository;

    @Spy
    private CarMapper carMapper = CarMapper.INSTANCE;

    private Clock fixedClock;
    private CarService carService; // no InjectMocks because clock is not a mock

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(Instant.parse("2025-10-21T00:00:00Z"), ZoneOffset.UTC);
        carService = new CarService(carMapper, carRepository, personRepository, fixedClock);
    }

    @Test
    void createCar_shouldSaveAndReturnDto() {
        // Arrange
        CarDto carDto = new CarDto(null, "BMW-35Q", 125, 5L);
        Car savedCar = new Car(1L, "BMW-35Q", 125, 5L);

        Person owner = new Person(5L, "Bob", LocalDate.of(1990, 1, 1));

        // I tell the repo what to return so that the Act doesn't fail
        when(personRepository.findById(5L)).thenReturn(Optional.of(owner));
        when(carRepository.save(any(Car.class))).thenReturn(savedCar);

        // Act
        CarDto result = carService.createCar(carDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getModel()).isEqualTo("BMW-35Q");
        assertThat(result.getHorsepower()).isEqualTo(125);
        assertThat(result.getOwnerId()).isEqualTo(5L);
        verify(carRepository).save(any(Car.class));
    }

    @Test
    void createCar_withDuplicatedId_shouldThrowException() {
        // Arrange
        Long duplicatedId = 1L;
        CarDto carDto = new CarDto(1L, "BMW-35Q", 125, 5L);

        // I tell the repo what to return so that the Act doesn't fail
        when(carRepository.existsById(duplicatedId)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> carService.createCar(carDto));

        assertThat(exception.getMessage()).isEqualTo("Car with ID 1 already exists");

        verify(carRepository).existsById(duplicatedId);
        verify(carRepository, never()).save(any(Car.class)); // check save is never called
    }

    @Test
    void createCar_checkOwnerExists_shouldThrowException() {
        // Arrange
        CarDto carDto = new CarDto(1L, "BMW-35Q", 125, 5L);

        when(personRepository.findById(carDto.getOwnerId())).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> carService.createCar(carDto));

        assertThat(exception.getMessage()).isEqualTo("Person with ID 5 not found");

        verify(carRepository, never()).save(any(Car.class)); // check save is never called
    }

    @Test
    void createCar_underageOwner_throwsIllegalArgumentException() {
        CarDto carDto = new CarDto(1L, "BMW-35Q", 125, 5L);
        Person young = new Person(5L, "Bobby", LocalDate.of(2020, 1, 1));

        when(personRepository.findById(5L)).thenReturn(Optional.of(young));

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> carService.createCar(carDto));

        assertEquals("Owner must be at least 18 years old", ex.getMessage());
        verify(carRepository, never()).save(any());
    }
}
