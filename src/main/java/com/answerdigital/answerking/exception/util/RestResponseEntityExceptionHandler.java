package com.answerdigital.answerking.exception.util;

import com.answerdigital.answerking.exception.AnswerKingException;
import com.answerdigital.answerking.exception.custom.ValidationException;
import com.answerdigital.answerking.exception.generic.InternalServerErrorException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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

    @ExceptionHandler({MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
        HttpMessageNotReadableException.class})
    public ValidationErrorResponse handleMethodArgumentNotValidException(

            final MethodArgumentNotValidException exception,
            final HttpServletRequest request
    ) {
        final Map<String, Collection<String>> errorsMap =
                exception.getBindingResult().getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(FieldError::getField,
                                fieldError -> new ArrayList<>(Collections.singletonList(fieldError.getDefaultMessage())),
                                (mainList, newList) -> {
                                    mainList.addAll(newList);
                                    return mainList;
                                }));

        return new ValidationErrorResponse(new ValidationException(errorsMap), request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ValidationErrorResponse handleConstraintViolationException(
            final ConstraintViolationException exception,
            final HttpServletRequest request
    ) {
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

        return new ValidationErrorResponse(new ValidationException(errorsMap), request);
    }

    @ExceptionHandler(AnswerKingException.class)
    public ErrorResponse handleAnswerKingException(
            final AnswerKingException exception,
            final HttpServletRequest request
    ) {
        return new ErrorResponse(exception, request);
    }

    // if an uncaught exception arrives here, default to a 500 Internal Server Error
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ErrorResponse defaultExceptionHandler(final Exception exception, final HttpServletRequest request) {
        return new ErrorResponse(new InternalServerErrorException(exception.getMessage()), request);
    }

}
