package com.project.rest_api.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.rest_api.repository.CarRepository;
import com.project.rest_api.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class ClearServiceTests {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private ClearService clearService;

    @Test
    void clearTable_removeCarsAndPeople() {

        clearService.clearTable();

        // order matters
        verify(carRepository).deleteAll();
        verify(personRepository).deleteAll();

        verifyNoMoreInteractions(carRepository, personRepository);
    }
}
