package com.project.rest_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {

    private Long id;

    /*
     * ^: Start of string. [^-]+: One or more characters that are not a hyphen. -: A literal hyphen.
     * $: End of string.
     */
    @NotNull(message = "model is required")
    @Pattern(regexp = "^[^-]+-[^-]+$",
            message = "Model must be in format 'vendor-model'. Vendor and model cannot be empty and vendor cannot contain '-'")
    @Size(max = 200, message = "model length is too large")
    private String model;

    @NotNull(message = "horsepower is required")
    @Positive(message = "horsepower must be greater than 0")
    @Max(value = 20000, message = "horsepower value is too large")
    private Integer horsepower;

    @NotNull(message = "ownerId is required")
    @Min(value = 1, message = "ownerId must be greater than 1")
    private Long ownerId;
}
