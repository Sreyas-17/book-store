package com.bookstore.bookstore_app.exception;

import com.bookstore.bookstore_app.dto.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        logger.warn("Validation failed for request: {}", request.getDescription(false));
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            logger.debug("Validation error - Field: {}, Message: {}", fieldName, errorMessage);
        });

        logger.info("Returning validation error response with {} field errors", errors.size());
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Validation failed", errors));
    }

    // Handle constraint violations
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        
        logger.warn("Constraint violation for request: {} - {}", request.getDescription(false), ex.getMessage());
        
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid input: " + ex.getMessage()));
    }

    // Handle bad request body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, WebRequest request) {
        
        logger.warn("Invalid request body for request: {} - {}", request.getDescription(false), ex.getMessage());
        
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid request format"));
    }

    // Handle entity not found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleEntityNotFound(
            EntityNotFoundException ex, WebRequest request) {
        
        logger.warn("Entity not found for request: {} - {}", request.getDescription(false), ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    // Handle data integrity violations (duplicate key, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, WebRequest request) {
        
        logger.error("Data integrity violation for request: {} - {}", request.getDescription(false), ex.getMessage());
        
        String message = "Data integrity error";
        if (ex.getMessage().contains("Duplicate entry")) {
            message = "This record already exists";
            logger.info("Duplicate entry detected, returning user-friendly message");
        }
        
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(message));
    }

    // Handle access denied
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDenied(
            AccessDeniedException ex, WebRequest request) {
        
        logger.warn("Access denied for request: {} - {}", request.getDescription(false), ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("Access denied"));
    }

    // Handle bad credentials
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentials(
            BadCredentialsException ex, WebRequest request) {
        
        logger.warn("Bad credentials for request: {} - {}", request.getDescription(false), ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Invalid credentials"));
    }

    // Handle custom business exceptions
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<String>> handleBusinessException(
            BusinessException ex, WebRequest request) {
        
        logger.warn("Business exception for request: {} - {}", request.getDescription(false), ex.getMessage());
        
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(ex.getMessage()));
    }

    // Handle generic runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        
        logger.error("Runtime exception for request: {} - {}", request.getDescription(false), ex.getMessage(), ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An error occurred: " + ex.getMessage()));
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGlobalException(
            Exception ex, WebRequest request) {
        
        logger.error("Unexpected exception for request: {} - {}", request.getDescription(false), ex.getMessage(), ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error"));
    }
}