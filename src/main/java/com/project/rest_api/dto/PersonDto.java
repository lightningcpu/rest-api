package com.project.rest_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {

    private Long id;

    @NotBlank(message = "name is required")
    @Size(max = 100, message = "name must be between 1 and 100 characters")
    private String name;

    @NotNull(message = "birthdate is required")
    @JsonFormat(pattern = "dd.MM.yyyy")
    @Past(message = "birthdate must be in the past")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthdate;
}
