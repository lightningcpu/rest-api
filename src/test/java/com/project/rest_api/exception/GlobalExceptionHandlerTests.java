package com.project.rest_api.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;

import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
        private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

        @Test
        void handleNotFound_forMissingPerson_returns404() {
                var exception = new ResourceNotFoundException("Person not found");
                var request = createRequest("/personwithcars");

                var response = handler.handleNotFound(exception, request);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(response.getBody().getMessage()).isEqualTo("Person not found");
                assertThat(response.getBody().getPath()).isEqualTo("/personwithcars");
        }

        @Test
        void handleIllegalArgument_forInvalidData_returns400() {
                var exception = new IllegalArgumentException("Invalid person data");
                var request = createRequest("/person");

                var response = handler.handleIllegalArgumentException(exception, request);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                assertThat(response.getBody().getMessage()).isEqualTo("Invalid person data");
        }

        @Test
        void handleConstraintViolation_forInvalidParameters_returns400() {
                var violations = new LinkedHashSet<>(List.of(
                                mockConstraintViolation("Person ID must be positive"),
                                mockConstraintViolation("Person ID must be provided")));
                var exception = new ConstraintViolationException(violations);
                var request = createRequest("/personwithcars");

                var response = handler.handleConstraintViolation(exception, request);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                var message = response.getBody().getMessage();
                assertThat(message).contains("Person ID must be positive",
                                "Person ID must be provided");
        }

        @Test
        void handleMethodArgumentNotValid_forInvalidCar_returns400() {
                var bindingResult = mock(BindingResult.class);
                when(bindingResult.getFieldErrors()).thenReturn(List.of(
                                new FieldError("car", "model", "Model cannot be empty"),
                                new FieldError("car", "horsepower", "Horsepower must be valid")));

                var exception = new MethodArgumentNotValidException(null, bindingResult);
                var webRequest = createWebRequest("/car");

                var response = handler.handleMethodArgumentNotValid(exception, new HttpHeaders(),
                                HttpStatus.BAD_REQUEST, webRequest);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                var errorBody = (ErrorResponse) response.getBody();
                assertThat(errorBody.getMessage())
                                .isEqualTo("Model cannot be empty; Horsepower must be valid");
                assertThat(errorBody.getPath()).isEqualTo("/car");
        }

        @Test
        void handleHttpMessageNotReadable_forBadJson_returns400() {
                var exception = new HttpMessageNotReadableException("Bad JSON",
                                new IllegalArgumentException("Invalid JSON format"), null);
                var webRequest = createWebRequest("/car");

                var response = handler.handleHttpMessageNotReadable(exception, new HttpHeaders(),
                                HttpStatus.BAD_REQUEST, webRequest);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                var errorBody = (ErrorResponse) response.getBody();
                assertThat(errorBody.getMessage()).startsWith(
                                "Malformed JSON or invalid field format: Invalid JSON format");
                assertThat(errorBody.getPath()).isEqualTo("/car");
        }

        @Test
        void handleAll_forUnexpectedError_returns500() {
                var exception = new RuntimeException("Database connection failed");
                var request = createRequest("/statistics");

                var response = handler.handleAll(exception, request);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                assertThat(response.getBody().getMessage())
                                .isEqualTo("An unexpected error occurred");
                assertThat(response.getBody().getPath()).isEqualTo("/statistics");
        }

        // Helper methods
        private ConstraintViolation<?> mockConstraintViolation(String message) {
                var violation = mock(ConstraintViolation.class);
                when(violation.getMessage()).thenReturn(message);
                return violation;
        }

        private MockHttpServletRequest createRequest(String path) {
                var request = new MockHttpServletRequest();
                request.setRequestURI(path);
                return request;
        }

        private ServletWebRequest createWebRequest(String path) {
                return new ServletWebRequest(createRequest(path));
        }
}
