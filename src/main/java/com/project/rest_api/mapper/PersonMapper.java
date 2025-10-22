package com.project.rest_api.mapper;

import com.project.rest_api.dto.PersonDto;
import com.project.rest_api.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonDto toDto(Person person);

    Person toEntity(PersonDto personDto);
}
