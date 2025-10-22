package com.project.rest_api.mapper;

import com.project.rest_api.dto.CarDto;
import com.project.rest_api.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    CarDto toDto(Car car);

    Car toEntity(CarDto carDto);
}
