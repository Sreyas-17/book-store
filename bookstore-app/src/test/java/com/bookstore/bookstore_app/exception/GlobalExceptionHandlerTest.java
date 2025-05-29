package com.bookstore.bookstore_app.exception;

import com.bookstore.bookstore_app.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Map;
import jakarta.persistence.EntityNotFoundException;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    public void testHandleValidationErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<ApiResponse<Map<String, String>>> response = handler.handleValidationErrors(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testHandleEntityNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<ApiResponse<String>> response = handler.handleEntityNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testHandleBusinessException() {
        BusinessException ex = new BusinessException("Business error");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<ApiResponse<String>> response = handler.handleBusinessException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
} 