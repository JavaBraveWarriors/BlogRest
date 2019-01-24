package com.blog.handler;

import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.response.ExceptionResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

@ControllerAdvice("com.blog")
public class RestErrorHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(DataAccessException.class)
    public final @ResponseBody
    ResponseEntity<Object> handleAllExceptions(DataAccessException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                Collections.singletonList(ex.getMessage()),
                request.getDescription(true));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public final @ResponseBody
    ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                Collections.singletonList(ex.getMessage()),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public final @ResponseBody
    ResponseEntity<ExceptionResponse> handleValidationException(ValidationException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                Collections.singletonList(ex.getMessage()),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(javax.validation.ValidationException.class)
    public final @ResponseBody
    ResponseEntity<ExceptionResponse> handleValidationExceptionObjects(javax.validation.ValidationException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                Collections.singletonList(ex.getMessage()),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerException.class)
    public final @ResponseBody
    ResponseEntity<ExceptionResponse> handleInternalServerException(InternalServerException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                Collections.singletonList(ex.getMessage()),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // If it is wrong to add links to the database (tag_id or post_id does not exist)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public final @ResponseBody
    ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                Collections.singletonList(ex.getMessage()),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionResponse errorDetails = new ExceptionResponse(
                Collections.singletonList(ex.getMessage()),
                ex.getBindingResult().toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
