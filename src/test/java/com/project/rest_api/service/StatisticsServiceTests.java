package com.project.rest_api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.rest_api.dto.StatisticsDto;
import com.project.rest_api.repository.CarRepository;
import com.project.rest_api.repository.PersonRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTests {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void geStatistics_WithData_ReturnsCorrectStatistics() {
        // Arrange
        Long expectedPersonCount = 10L;
        Long expectedCarCount = 25L;
        Long expectedUniqueVendorCount = 5L;

        when(personRepository.count()).thenReturn(expectedPersonCount);
        when(carRepository.count()).thenReturn(expectedCarCount);
        when(carRepository.countDistinctModels()).thenReturn(expectedUniqueVendorCount);

        // Act
        StatisticsDto result = statisticsService.getStatistics();

        // Assert
        assertThat(result.getPersoncount()).isEqualTo(expectedPersonCount);
        assertThat(result.getCarcount()).isEqualTo(expectedCarCount);
        assertThat(result.getUniquevendorcount()).isEqualTo(expectedUniqueVendorCount);

        // Verify interactions
        verify(personRepository).count();
        verify(carRepository).count();
        verify(carRepository).countDistinctModels();
    }

    @Test
    void geStatistics_NoData_ReturnsZeroStatistics() {
        // Arrange
        Long expectedPersonCount = 0L;
        Long expectedCarCount = 0L;
        Long expectedUniqueVendorCount = 0L;

        when(personRepository.count()).thenReturn(expectedPersonCount);
        when(carRepository.count()).thenReturn(expectedCarCount);
        when(carRepository.countDistinctModels()).thenReturn(expectedUniqueVendorCount);

        // Act
        StatisticsDto result = statisticsService.getStatistics();

        // Assert
        assertThat(result.getPersoncount()).isEqualTo(0L);
        assertThat(result.getCarcount()).isEqualTo(0L);
        assertThat(result.getUniquevendorcount()).isEqualTo(0L);

        // Verify interactions
        verify(personRepository).count();
        verify(carRepository).count();
        verify(carRepository).countDistinctModels();
    }
}
