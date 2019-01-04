package com.blog.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ValidationException extends RuntimeException {

    private static final Logger LOGGER = LogManager.getLogger();

    public ValidationException(String message) {
        super(message);
        LOGGER.error(message);

    }
}
