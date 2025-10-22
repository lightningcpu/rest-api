package com.project.rest_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.rest_api.dto.PersonDto;
import com.project.rest_api.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PersonController.class)
class PersonControllerTests {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private PersonService personService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void createPerson_whenValidInput_thenReturns200() throws Exception {
                // Arrange
                PersonDto request = new PersonDto(null, "Alice", LocalDate.of(1990, 10, 17));
                PersonDto response = new PersonDto(1L, "Alice", LocalDate.of(1990, 10, 17));

                when(personService.createPerson(any(PersonDto.class))).thenReturn(response);

                // Act & Assert
                mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.name").value("Alice"))
                                .andExpect(jsonPath("$.birthdate").value("17.10.1990"));

                verify(personService).createPerson(any(PersonDto.class));
        }

        @Test
        void createPerson_whenMissingName_thenReturns400() throws Exception {
                // Arrange
                PersonDto request = new PersonDto(null, null, LocalDate.of(1990, 10, 17));

                // Act & Assert
                mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.message", containsString("name")));

                verifyNoInteractions(personService);
        }

        @Test
        void createPerson_whenBadDateFormat_thenReturns400() throws Exception {
                // Arrange
                String body = """
                                {
                                  "name": "Bob",
                                  "birthdate": "1990-10-17"
                                }
                                """;

                // Act & Assert
                mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                                .content(body)).andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message",
                                                containsStringIgnoringCase("malformed")));

                verifyNoInteractions(personService);
        }

        @Test
        void createPerson_whenNameTooLong_thenReturns400() throws Exception {
                // Arrange
                String longName = "A".repeat(101);
                PersonDto request = new PersonDto(null, longName, LocalDate.of(1990, 10, 17));

                // Act & Assert
                mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400));

                verifyNoInteractions(personService);
        }

        @Test
        void createPerson_whenBirthdateInFuture_thenReturns400() throws Exception {
                // Arrange
                LocalDate futureDate = LocalDate.now().plusDays(1);
                PersonDto request = new PersonDto(null, "Charlie", futureDate);

                // Act & Assert
                mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400));

                verifyNoInteractions(personService);
        }

        @Test
        void createPerson_whenEmptyName_thenReturns400() throws Exception {
                // Arrange
                PersonDto request = new PersonDto(null, "", LocalDate.of(1990, 10, 17));

                // Act & Assert
                mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400));

                verifyNoInteractions(personService);
        }

        @Test
        void createPerson_whenNullBirthdate_thenReturns400() throws Exception {
                // Arrange
                PersonDto request = new PersonDto(null, "Alice", null);

                // Act & Assert
                mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400));

                verifyNoInteractions(personService);
        }
}
