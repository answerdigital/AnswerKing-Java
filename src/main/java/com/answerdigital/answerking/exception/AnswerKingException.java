package com.answerdigital.answerking.exception;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AnswerKingException extends RuntimeException {

    private final String type;

    private final String title;

    private final HttpStatus status;

    private final String detail;

    private final Map<String, Collection<String>> errors = new HashMap<>();
    private final String error;

    protected AnswerKingException(
            final String type,
            final String title,
            final HttpStatus status,
            final String detail,
            final Map<String, Collection<String>> errors
    ) {
        super(errors.toString());
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.errors.putAll(errors);
        this.error = "";
    }
    protected AnswerKingException(
            final String type,
            final String title,
            final HttpStatus status,
            final String detail,
            final String error
    ) {
        super(error);
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.error = error;
    }
}
