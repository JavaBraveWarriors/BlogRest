package com.blog.exception;

/**
 * The Internal server exception.
 */
public class InternalServerException extends RuntimeException {

    public InternalServerException(String message) {
        super(message);
    }
}
