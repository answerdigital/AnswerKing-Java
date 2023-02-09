package com.answerdigital.answerking.exception.custom;

import com.answerdigital.answerking.exception.generic.BadRequestException;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;

/**
 * The class ValidationException represents an exception when one or more validation exceptions occur.
 */
@Getter
public class ValidationException extends BadRequestException {

    private static final String TITLE = "Validation Exception";

    private static final String DETAIL = "One or more validation errors occurred";

    private final Map<String, Collection<String>> errors;

    public ValidationException(final Map<String, Collection<String>> errors) {
        super(DETAIL);
        this.errors = errors;
    }
}
