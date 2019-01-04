package com.blog.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InternalServerException extends RuntimeException {
    private static final Logger LOGGER = LogManager.getLogger();

    public InternalServerException(String message) {
        super(message);
        LOGGER.error(message);
    }
}
