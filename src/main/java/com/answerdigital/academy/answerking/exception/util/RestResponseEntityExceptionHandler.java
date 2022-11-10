package com.answerdigital.academy.answerking.exception.util;

import com.answerdigital.academy.answerking.exception.AnswerKingException;
import com.answerdigital.academy.answerking.exception.generic.BadRequestException;
import com.answerdigital.academy.answerking.exception.generic.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception,
            final HttpServletRequest request
    ) {
        String detail;
        try {
            detail = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        } catch (NullPointerException nullPointerException) {
            detail = "Unknown error - MethodArgumentNotValidException was thrown with no default message";
        }

        final ErrorResponse response = new ErrorResponse(new BadRequestException(detail), request);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            final ConstraintViolationException exception,
            final HttpServletRequest request
    ) {
        final List<String> errorMessages = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        final ErrorResponse response = new ErrorResponse(new BadRequestException(errorMessages), request);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AnswerKingException.class)
    public ResponseEntity<ErrorResponse> handleAnswerKingException(
            final AnswerKingException exception,
            final HttpServletRequest request
    ) {
        final ErrorResponse response = new ErrorResponse(exception, request);
        return new ResponseEntity<>(response, exception.getStatus());
    }

    // if an uncaught exception arrives here, default to a 500 Internal Server Error
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ResponseEntity<ErrorResponse> defaultExceptionHandler(
            final Exception exception,
            final HttpServletRequest request
    ) {
        final ErrorResponse response = new ErrorResponse(new InternalServerErrorException(exception.getMessage()), request);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
