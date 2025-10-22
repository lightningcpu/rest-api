package com.project.rest_api.mapper;

import org.junit.jupiter.api.Test;
import com.project.rest_api.dto.PersonDto;
import com.project.rest_api.entity.Person;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class PersonMapperTest {

    private PersonMapper mapper = PersonMapper.INSTANCE;

    @Test
    void personMapper_EntityToDto() {
        // Arrange
        Person entity = new Person(42L, "Alice", LocalDate.of(2000, 1, 2));

        // Act
        PersonDto dto = mapper.toDto(entity);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(42L);
        assertThat(dto.getName()).isEqualTo("Alice");
        assertThat(dto.getBirthdate()).isEqualTo(LocalDate.of(2000, 1, 2));
    }

    @Test
    void personMapper_DtoToEntity() {
        // Arrange
        PersonDto dto = new PersonDto(36L, "Bob", LocalDate.of(1990, 5, 6));

        // Act
        Person entity = mapper.toEntity(dto);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(36L);
        assertThat(entity.getName()).isEqualTo("Bob");
        assertThat(entity.getBirthdate()).isEqualTo(LocalDate.of(1990, 5, 6));
    }

    @Test
    void personMapper_NullEntityToDto() {
        assertThat(mapper.toDto((Person) null)).isNull();
    }

    @Test
    void personMapper_NullDtoToEntity() {
        assertThat(mapper.toEntity((PersonDto) null)).isNull();
    }
}
