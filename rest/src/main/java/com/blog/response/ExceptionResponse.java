package com.blog.response;


import java.util.List;

public class ExceptionResponse {

    private List<String> message;

    private String details;

    public ExceptionResponse() {
    }

    public ExceptionResponse(List<String> message, String details) {
        this.message = message;
        this.details = details;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
