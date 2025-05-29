package com.bookstore.bookstore_app.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BusinessException extends RuntimeException {
    
    private static final Logger logger = LogManager.getLogger(BusinessException.class);

    public BusinessException(String message) {
        super(message);
        logger.debug("BusinessException created with message: {}", message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        logger.debug("BusinessException created with message: {} and cause: {}", message, cause.getMessage());
    }
}