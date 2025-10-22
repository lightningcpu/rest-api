package com.project.rest_api.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.project.rest_api.entity.Person;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PersonRepositoryTests {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void PersonRepository_SavePerson_ReturnsSavedPerson() {

        // Arrange
        Person person = new Person(null, "Alice", LocalDate.of(2005, 10, 17));

        // Act
        Person savedPerson = personRepository.save(person);

        // Assert
        assertThat(savedPerson).isNotNull();
        assertThat(savedPerson.getId()).isGreaterThan(0);
        assertThat(savedPerson.getName()).isEqualTo("Alice");
        assertThat(savedPerson.getBirthdate()).isEqualTo(LocalDate.of(2005, 10, 17));


    }

    @Test
    public void PersonRepository_GetAll_ReturnsMoreThanOnePerson() {

        // Arrange
        Person person1 = new Person(null, "Alice", LocalDate.of(2005, 10, 17));
        Person person2 = new Person(null, "Alex", LocalDate.of(2005, 12, 17));

        // Act
        personRepository.save(person1);
        personRepository.save(person2);

        List<Person> personList = personRepository.findAll();

        // Assert
        assertThat(personList).isNotNull();
        assertThat(personList.size()).isEqualTo(2);
    }

    @Test
    public void PersonRepository_ClearAll_RemovesAllPersonEntries() {
        // Arrange
        Person person1 = new Person(null, "Alice", LocalDate.of(2005, 10, 17));
        Person person2 = new Person(null, "Alex", LocalDate.of(2005, 12, 17));

        personRepository.save(person1);
        personRepository.save(person2);

        // Act
        personRepository.deleteAll();
        List<Person> checkThatDeleted = personRepository.findAll();

        // Assert
        assertThat(checkThatDeleted).isEmpty();

    }
}
