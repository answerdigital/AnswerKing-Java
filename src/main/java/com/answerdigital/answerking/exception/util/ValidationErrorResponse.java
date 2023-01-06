package com.answerdigital.answerking.exception.util;

import com.answerdigital.answerking.exception.custom.ValidationException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class ValidationErrorResponse extends ErrorResponse {

    public ValidationErrorResponse(final ValidationException exception, final HttpServletRequest request) {
        super(exception, request);
        setProperty("errors", exception.getErrors());

        log.error(String.format("PROPERTIES: %s - TRACEID: %s", getProperties(), getTraceId()));
    }
}
