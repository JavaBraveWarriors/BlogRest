package com.blog.exception;

/**
 * The Not found exception.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
