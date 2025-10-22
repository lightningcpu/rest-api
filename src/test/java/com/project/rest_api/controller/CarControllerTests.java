package com.project.rest_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.rest_api.dto.CarDto;
import com.project.rest_api.service.CarService;
import com.project.rest_api.service.PersonService;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

@WebMvcTest(controllers = CarController.class)
public class CarControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    @MockitoBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCar_whenValidInput_thenReturns200() throws Exception {

        CarDto request = new CarDto(null, "BMW-35Q", 125, 1L);
        CarDto response = new CarDto(1L, "BMW-35Q", 125, 1L);

        when(carService.createCar(any(CarDto.class))).thenReturn(response);


        mockMvc.perform(post("/car").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.model").value("BMW-35Q"))
                .andExpect(jsonPath("$.horsepower").value(125))
                .andExpect(jsonPath("$.ownerId").value(1));

        verify(carService).createCar(any(CarDto.class));
    }

    @Test
    void createCar_whenMissingModel_thenReturns400() throws Exception {

        CarDto request = new CarDto(null, null, 125, 1L);

        mockMvc.perform(post("/car").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("model")));

        verifyNoInteractions(carService);
    }

    @Test
    void createCar_whenMissingHorsepower_thenReturns400() throws Exception {

        CarDto request = new CarDto(null, "BMW-35Q", null, 1L);

        mockMvc.perform(post("/car").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("horsepower")));

        verifyNoInteractions(carService);
    }

    @Test
    void createCar_whenMissingOwnerId_thenReturns400() throws Exception {

        CarDto request = new CarDto(null, "BMW-35Q", 125, null);

        mockMvc.perform(post("/car").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("ownerId")));

        verifyNoInteractions(carService);
    }

    @Test
    void createCar_whenBadModelInput_thenReturns400() throws Exception {

        CarDto request = new CarDto(null, "BMW", 125, 1L);

        mockMvc.perform(post("/car").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("format")));

        verifyNoInteractions(carService);
    }

    @Test
    void createCar_whenBadModelInput2_thenReturns400() throws Exception {

        CarDto request = new CarDto(null, "BMW-MER-25", 125, 1L);

        mockMvc.perform(post("/car").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("format")));

        verifyNoInteractions(carService);
    }

    @Test
    void createCar_whenAbsurdHorsepower_thenReturns400() throws Exception {

        CarDto request = new CarDto(null, "BMW-35Q", 99999, 1L);

        mockMvc.perform(post("/car").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("horsepower")));

        verifyNoInteractions(carService);
    }
}
