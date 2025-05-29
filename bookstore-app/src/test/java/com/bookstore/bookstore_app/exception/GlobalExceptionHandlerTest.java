package com.bookstore.bookstore_app.exception;

import com.bookstore.bookstore_app.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    public void testHandleValidationErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.emptyList());
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<ApiResponse<Map<String, String>>> response = handler.handleValidationErrors(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals(0, response.getBody().getData().size());
    }

    @Test
    public void testHandleValidationErrorsWithErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "Invalid field");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<ApiResponse<Map<String, String>>> response = handler.handleValidationErrors(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals("Invalid field", response.getBody().getData().get("fieldName"));
    }

    @Test
    public void testHandleEntityNotFound() {
        jakarta.persistence.EntityNotFoundException ex = new jakarta.persistence.EntityNotFoundException("Entity not found");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<ApiResponse<String>> response = handler.handleEntityNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Entity not found", response.getBody().getMessage());
    }

    @Test
    public void testHandleBusinessException() {
        BusinessException ex = new BusinessException("Business error");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<ApiResponse<String>> response = handler.handleBusinessException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Business error", response.getBody().getMessage());
    }
}