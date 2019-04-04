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

/**
 * The Rest error handler tracks and catches all errors that occur in the {com.blog} package.
 *
 * @see ResponseEntityExceptionHandler
 */
@ControllerAdvice("com.blog")
public class RestErrorHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle data access exception. Throw if an error occurred with data access in dao.
     *
     * @param ex      is {DataAccessException}.
     * @param request the web request with which an error occurred.
     * @return the response entity with message of exception.
     */
    @ExceptionHandler(DataAccessException.class)
    public final @ResponseBody
    ResponseEntity<ExceptionResponse> handleDataAccessException(DataAccessException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                Collections.singletonList(ex.getMessage()),
                request.getDescription(true));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle not found exception. Throw if something is not found when querying the database.
     *
     * @param ex      is {NotFoundException}.
     * @param request the web request with which an error occurred.
     * @return the response entity with message of exception.
     */
    @ExceptionHandler(NotFoundException.class)
    public final @ResponseBody
    ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                Collections.singletonList(ex.getMessage()),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle validation exception. Throw if there was an error validating the data in the request.
     *
     * @param ex      is {ValidationException}.
     * @param request the web request with which an error occurred.
     * @return the response entity with message of exception.
     */
    @ExceptionHandler(ValidationException.class)
    public final @ResponseBody
    ResponseEntity<ExceptionResponse> handleValidationException(ValidationException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                Collections.singletonList(ex.getMessage()),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle validation exception. Throw if there was an error validating the data of the object supplied in the request body.
     *
     * @param ex      is {javax.validation.ValidationException}.
     * @param request the web request with which an error occurred.
     * @return the response entity with message of exception.
     */
    @ExceptionHandler(javax.validation.ValidationException.class)
    public final @ResponseBody
    ResponseEntity<ExceptionResponse> handleValidationExceptionObjects(javax.validation.ValidationException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                Collections.singletonList(ex.getMessage()),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle internal server exception. Throw if an error occurred on the server side.
     *
     * @param ex      is {InternalServerException}.
     * @param request the web request with which an error occurred.
     * @return the response entity with message of exception.
     */
    @ExceptionHandler(InternalServerException.class)
    public final @ResponseBody
    ResponseEntity<ExceptionResponse> handleInternalServerException(InternalServerException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                Collections.singletonList(ex.getMessage()),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle data integrity violation exception. Throw if it is wrong to add links to the database (tag_id or post_id does not exist).
     *
     * @param ex      is {DataIntegrityViolationException}.
     * @param request the web request with which an error occurred.
     * @return the response entity with message of exception.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public final @ResponseBody
    ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                Collections.singletonList(ex.getMessage()),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handle method argument not valid exception. Throw if it is wrong to add links to the database (tag_id or post_id does not exist).
     *
     * @param ex      is {MethodArgumentNotValidException}.
     * @param headers headers that were in the request.
     * @param status  to compare statuses.
     * @param request the web request with which an error occurred.
     * @return the response entity with message of exception.
     */
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionResponse errorDetails = new ExceptionResponse(
                Collections.singletonList(ex.getMessage()),
                ex.getBindingResult().toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}