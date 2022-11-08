package com.answerdigital.academy.answerking.exception.util;

import com.answerdigital.academy.answerking.exception.AnswerKingException;
import com.answerdigital.academy.answerking.exception.generic.BadRequestException;
import com.answerdigital.academy.answerking.exception.generic.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        String detail;
        try {
            detail = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
        } catch (NullPointerException nullPointerException) {
            detail = "Unknown error - MethodArgumentNotValidException was thrown with no default message";
        }

        final ErrorResponse response = new ErrorResponse(new BadRequestException(detail));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(final ConstraintViolationException e) {
        final List<String> errorMessages = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        final ErrorResponse response = new ErrorResponse(new BadRequestException(errorMessages));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AnswerKingException.class)
    public ResponseEntity<ErrorResponse> handleAnswerKingException(final AnswerKingException e) {
        final ErrorResponse response = new ErrorResponse(e);
        return new ResponseEntity<>(response, e.getStatus());
    }

    // if an uncaught exception arrives here, default to a 500 Internal Server Error
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ResponseEntity<ErrorResponse> defaultExceptionHandler(final Exception e) {
        final ErrorResponse response = new ErrorResponse(new InternalServerErrorException(e.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
