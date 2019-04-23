package com.blog.response;

import java.util.List;

/**
 * The Exception response.
 */
public class ExceptionResponse {

    private List<String> message;

    private String details;

    public ExceptionResponse(List<String> message, String details) {
        this.message = message;
        this.details = details;
    }

    public List<String> getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}