package com.answerdigital.answerking.exception;

import java.util.Collection;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AnswerKingException extends RuntimeException {

    private final String type;

    private final String title;

    private final HttpStatus status;

    private final String detail;

    private final Collection<String> errors;

    protected AnswerKingException(
            final String type,
            final String title,
            final HttpStatus status,
            final String detail,
            final Collection<String> errors
    ) {
        super(errors.toString());
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.errors = errors;
    }
}
