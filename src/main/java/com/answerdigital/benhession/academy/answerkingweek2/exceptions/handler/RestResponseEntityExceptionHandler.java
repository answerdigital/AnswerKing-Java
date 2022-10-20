package com.answerdigital.benhession.academy.answerkingweek2.exceptions.handler;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ConflictException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.response.ErrorResponse;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ItemUnavailableException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;


@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception,
                                                                            HttpServletRequest request) {
        List<String> errorMessages = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessages.toString(), request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(RuntimeException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(HttpStatus.CONFLICT, e.getMessage(), request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(ItemUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleItemUnavailableException(RuntimeException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(HttpStatus.FORBIDDEN, e.getMessage(), request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}