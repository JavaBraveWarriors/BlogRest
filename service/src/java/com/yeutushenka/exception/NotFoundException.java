package com.yeutushenka.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotFoundException extends RuntimeException {

    private static final Logger LOGGER = LogManager.getLogger();

    public NotFoundException(String massage) {
        super(massage);
        LOGGER.error(massage);
    }
}
