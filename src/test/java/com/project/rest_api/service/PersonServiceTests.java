package com.project.rest_api.service;

import com.project.rest_api.dto.PersonDto;
import com.project.rest_api.entity.Person;
import com.project.rest_api.mapper.PersonMapper;
import com.project.rest_api.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTests {

    @Mock
    private PersonRepository personRepository;

    @Spy
    private PersonMapper personMapper = PersonMapper.INSTANCE;

    @InjectMocks
    private PersonService personService;

    @Test
    void createPerson_shouldSaveAndReturnDto() {
        // Arrange
        PersonDto personDto = new PersonDto(null, "Alice", LocalDate.of(2005, 10, 17));
        Person savedPerson = new Person(1L, "Alice", LocalDate.of(2005, 10, 17));

        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        // Act
        PersonDto result = personService.createPerson(personDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Alice");
        assertThat(result.getBirthdate()).isEqualTo(LocalDate.of(2005, 10, 17));
        verify(personRepository).save(any(Person.class));
    }

    @Test
    void createPerson_withDifferentData_shouldReturnCorrectDto() {
        // Arrange
        PersonDto personDto = new PersonDto(null, "Bob", LocalDate.of(1985, 5, 15));
        Person savedPerson = new Person(2L, "Bob", LocalDate.of(1985, 5, 15));

        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        // Act
        PersonDto result = personService.createPerson(personDto);

        // Assert
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Bob");
        assertThat(result.getBirthdate()).isEqualTo(LocalDate.of(1985, 5, 15));
    }

    @Test
    void createPerson_withVeryOldBirthdate_shouldWorkCorrectly() {
        // Arrange
        PersonDto personDto = new PersonDto(null, "Elder", LocalDate.of(1900, 1, 1));
        Person savedPerson = new Person(3L, "Elder", LocalDate.of(1900, 1, 1));

        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        // Act
        PersonDto result = personService.createPerson(personDto);

        // Assert
        assertThat(result.getBirthdate()).isEqualTo(LocalDate.of(1900, 1, 1));
        assertThat(result.getBirthdate()).isBefore(LocalDate.now());
    }

    @Test
    void createPerson_withMinimumNameLength_shouldWorkCorrectly() {
        // Arrange
        PersonDto personDto = new PersonDto(null, "A", LocalDate.of(2000, 1, 1));
        Person savedPerson = new Person(4L, "A", LocalDate.of(2000, 1, 1));

        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        // Act
        PersonDto result = personService.createPerson(personDto);

        // Assert
        assertThat(result.getName()).isEqualTo("A");
        assertThat(result.getName()).hasSize(1);
    }

    @Test
    void createPerson_withMaximumNameLength_shouldWorkCorrectly() {
        // Arrange
        String maxLengthName = "A".repeat(100);
        PersonDto personDto = new PersonDto(null, maxLengthName, LocalDate.of(2000, 1, 1));
        Person savedPerson = new Person(5L, maxLengthName, LocalDate.of(2000, 1, 1));

        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        // Act
        PersonDto result = personService.createPerson(personDto);

        // Assert
        assertThat(result.getName()).isEqualTo(maxLengthName);
        assertThat(result.getName()).hasSize(100);
    }

    @Test
    void createPerson_withDuplicateId_shouldThrowException() {
        // Arrange
        Long duplicateId = 1L;
        PersonDto personDto = new PersonDto(duplicateId, "Alice", LocalDate.of(2005, 10, 17));

        when(personRepository.existsById(duplicateId)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> personService.createPerson(personDto));

        assertThat(exception.getMessage()).isEqualTo("Person with ID 1 already exists");
        verify(personRepository).existsById(duplicateId);
        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void createPerson_withNullId_shouldNotCheckForDuplicates() {
        // Arrange
        PersonDto personDto = new PersonDto(null, "Bob", LocalDate.of(1985, 5, 15));
        Person savedPerson = new Person(2L, "Bob", LocalDate.of(1985, 5, 15));

        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        // Act
        PersonDto result = personService.createPerson(personDto);

        // Assert
        assertThat(result).isNotNull();
        verify(personRepository, never()).existsById(any());
        verify(personRepository).save(any(Person.class));
    }

    @Test
    void createPerson_withNewId_shouldSaveSuccessfully() {
        // Arrange
        Long newId = 3L;
        PersonDto personDto = new PersonDto(newId, "Charlie", LocalDate.of(1990, 3, 20));
        Person savedPerson = new Person(newId, "Charlie", LocalDate.of(1990, 3, 20));

        when(personRepository.existsById(newId)).thenReturn(false);
        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        // Act
        PersonDto result = personService.createPerson(personDto);

        // Assert
        assertThat(result.getId()).isEqualTo(newId);
        verify(personRepository).existsById(newId);
        verify(personRepository).save(any(Person.class));
    }

    @Test
    void createPerson_shouldCallRepositoryOnce() {
        // Arrange
        PersonDto personDto = new PersonDto(null, "Test", LocalDate.of(1999, 12, 31));
        Person savedPerson = new Person(6L, "Test", LocalDate.of(1999, 12, 31));

        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        // Act
        personService.createPerson(personDto);

        // Assert
        verify(personRepository, times(1)).save(any(Person.class));
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void createPerson_shouldMapAllFieldsCorrectly() {
        // Arrange
        PersonDto personDto = new PersonDto(null, "MappingTest", LocalDate.of(1975, 8, 25));
        Person savedPerson = new Person(7L, "MappingTest", LocalDate.of(1975, 8, 25));

        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        // Act
        PersonDto result = personService.createPerson(personDto);

        // Assert
        assertThat(result).isNotNull().satisfies(dto -> {
            assertThat(dto.getId()).isEqualTo(7L);
            assertThat(dto.getName()).isEqualTo("MappingTest");
            assertThat(dto.getBirthdate()).isEqualTo(LocalDate.of(1975, 8, 25));
        });
    }
}
