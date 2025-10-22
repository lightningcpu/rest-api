package com.project.rest_api.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

        // Helper method to create error responses consistently
        private ErrorResponse createErrorResponse(HttpStatus status, String message, String path) {
                return new ErrorResponse(OffsetDateTime.now(), status.value(),
                                status.getReasonPhrase(), message, path);
        }

        private String getRequestPath(WebRequest request) {
                return ((ServletWebRequest) request).getRequest().getRequestURI();
        }

        // Handles @Valid errors on @RequestBody
        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException ex, HttpHeaders headers,
                        HttpStatusCode status, WebRequest request) {

                String messages = ex.getBindingResult().getFieldErrors().stream()
                                .map(fieldError -> fieldError.getDefaultMessage())
                                .collect(Collectors.joining("; "));

                ErrorResponse body = createErrorResponse(HttpStatus.BAD_REQUEST, messages,
                                getRequestPath(request));
                return ResponseEntity.badRequest().headers(headers).body(body);
        }

        // Handles malformed JSON / invalid format
        @Override
        protected ResponseEntity<Object> handleHttpMessageNotReadable(
                        HttpMessageNotReadableException ex, HttpHeaders headers,
                        HttpStatusCode status, WebRequest request) {

                String message = "Malformed JSON or invalid field format: "
                                + ex.getMostSpecificCause().getMessage();
                ErrorResponse body = createErrorResponse(HttpStatus.BAD_REQUEST, message,
                                getRequestPath(request));

                return ResponseEntity.badRequest().headers(headers).body(body);
        }

        // Handles programmatic constraint violations
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponse> handleConstraintViolation(
                        ConstraintViolationException ex, HttpServletRequest request) {

                String messages = ex.getConstraintViolations().stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining("; "));

                ErrorResponse body = createErrorResponse(HttpStatus.BAD_REQUEST, messages,
                                request.getRequestURI());
                return ResponseEntity.badRequest().body(body);
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex,
                        HttpServletRequest request) {

                ErrorResponse body = createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(),
                                request.getRequestURI());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
                        IllegalArgumentException ex, HttpServletRequest request) {

                ErrorResponse body = createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(),
                                request.getRequestURI());
                return ResponseEntity.badRequest().body(body);
        }

        // Fallback for unexpected exceptions
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
                log.error("Unhandled error", ex);

                ErrorResponse body = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                "An unexpected error occurred", request.getRequestURI());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
}
