package com.answerdigital.answerking.exception.util;

import com.answerdigital.answerking.exception.AnswerKingException;
import com.answerdigital.answerking.exception.custom.ValidationException;
import com.answerdigital.answerking.exception.generic.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception,
            final HttpServletRequest request) {

        final Map<String, Collection<String>> errorsMap =
                exception.getBindingResult().getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(FieldError::getField,
                                FieldError -> new ArrayList<>(Collections.singletonList(FieldError.getDefaultMessage())),
                                (l1, l2) -> {
                                    l1.addAll(l2);
                                    return l1;
                                }));

        final ErrorResponse response = new ValidationErrorResponse(new ValidationException(errorsMap), request);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            final ConstraintViolationException exception,
            final HttpServletRequest request) {

        final Map<String, Collection<String>> errorsMap = new HashMap<>();
        for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
            final String mapKey = Objects.requireNonNull(StreamSupport
                    .stream(constraintViolation.getPropertyPath().spliterator(), false)
                    .reduce((first, second) -> second)
                    .orElse(null)).toString();

            if (errorsMap.containsKey(mapKey)) {
                errorsMap.get(mapKey).add(constraintViolation.getMessage());
            } else {
                final Collection<String> newCollection = new ArrayList<>();
                newCollection.add(constraintViolation.getMessage());
                errorsMap.put(mapKey, newCollection);
            }
        }

        final ErrorResponse response = new ValidationErrorResponse(new ValidationException(errorsMap), request);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AnswerKingException.class)
    public ResponseEntity<ErrorResponse> handleAnswerKingException(
            final AnswerKingException exception,
            final HttpServletRequest request) {

        final ErrorResponse response = new ErrorResponse(exception, request);
        return new ResponseEntity<>(response, exception.getStatus());
    }

    // if an uncaught exception arrives here, default to a 500 Internal Server Error
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ResponseEntity<ErrorResponse> defaultExceptionHandler(
            final Exception exception,
            final HttpServletRequest request) {

        final ErrorResponse response = new ErrorResponse(new InternalServerErrorException(exception.getMessage()), request);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
