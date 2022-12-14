package com.answerdigital.answerking.exception.util;

import com.answerdigital.answerking.exception.custom.ValidationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

@Slf4j
@Getter
public class ValidationErrorResponse extends ErrorResponse {

    private final Map<String, Collection<String>> errors;

    public ValidationErrorResponse(final ValidationException exception, final HttpServletRequest request) {
        super(exception, request);
        this.errors = exception.getErrors();
    }
}
