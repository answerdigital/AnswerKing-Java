package com.answerdigital.academy.answerking.exception.handler;

import com.answerdigital.academy.answerking.exception.ConflictException;
import com.answerdigital.academy.answerking.exception.response.ErrorResponse;
import com.answerdigital.academy.answerking.exception.ItemUnavailableException;
import com.answerdigital.academy.answerking.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex, final HttpHeaders headers,
            final HttpStatus status, final WebRequest request) {
        String error;
        try {
            error = Objects.requireNonNull(ex.getFieldError()).getDefaultMessage();
        } catch (NullPointerException e) {
            error = "Unknown error";
        }

        final ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, error, (
                (ServletWebRequest)request).getRequest().getRequestURI());
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(final ConstraintViolationException exception,
                                                                            final HttpServletRequest request) {
        final List<String> errorMessages = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        final ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, errorMessages.toString(), request.getRequestURI());
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final RuntimeException e, final HttpServletRequest request) {
        final ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(final RuntimeException e, final HttpServletRequest request) {
        final ErrorResponse response = new ErrorResponse(HttpStatus.CONFLICT, e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(ItemUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleItemUnavailableException(
            final RuntimeException e, final HttpServletRequest request) {
        final ErrorResponse response = new ErrorResponse(HttpStatus.FORBIDDEN, e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    // if an uncaught exception arrives here, default to a 500 Internal Server Error
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ResponseEntity<ErrorResponse> defaultExceptionHandler(final Exception e, final HttpServletRequest request) {
        final ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
