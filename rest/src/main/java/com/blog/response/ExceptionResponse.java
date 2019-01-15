package com.blog.response;


import java.time.LocalDate;
import java.util.List;

public class ExceptionResponse {
    private LocalDate time;
    private List<String> message;
    private String details;

    public ExceptionResponse(LocalDate time, List<String> message, String details) {
        this.time = time;
        this.message = message;
        this.details = details;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
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
