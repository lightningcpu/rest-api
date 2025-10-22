package com.project.rest_api.mapper;

import org.junit.jupiter.api.Test;
import com.project.rest_api.dto.CarDto;
import com.project.rest_api.entity.Car;
import static org.assertj.core.api.Assertions.assertThat;

class CarMapperTest {

    private CarMapper mapper = CarMapper.INSTANCE;

    @Test
    void personMapper_EntityToDto() {
        // Arrange
        Car entity = new Car(24L, "BMW-25Q", 125, 13L);

        // Act
        CarDto dto = mapper.toDto(entity);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(24L);
        assertThat(dto.getModel()).isEqualTo("BMW-25Q");
        assertThat(dto.getHorsepower()).isEqualTo(125);
        assertThat(dto.getOwnerId()).isEqualTo(13L);
    }

    @Test
    void personMapper_DtoToEntity() {
        // Arrange
        CarDto dto = new CarDto(24L, "BMW-25Q", 125, 13L);

        // Act
        Car entity = mapper.toEntity(dto);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(24L);
        assertThat(entity.getModel()).isEqualTo("BMW-25Q");
        assertThat(entity.getHorsepower()).isEqualTo(125);
        assertThat(entity.getOwnerId()).isEqualTo(13L);
    }

    @Test
    void carMapper_NullEntityToDto() {
        assertThat(mapper.toDto((Car) null)).isNull();
    }

    @Test
    void carMapper_NullDtoToEntity() {
        assertThat(mapper.toEntity((CarDto) null)).isNull();
    }
}
