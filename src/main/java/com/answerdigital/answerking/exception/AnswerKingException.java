package com.answerdigital.answerking.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AnswerKingException extends RuntimeException {

    private final String type;

    private final String title;

    private final HttpStatus status;

    private final String errorMessageId;

    private final String detail;

    protected AnswerKingException(
            final String type,
            final String title,
            final HttpStatus status,
            final String errorMessageId,
            final String errorMessage
    ) {
        super(errorMessage);
        this.type = type;
        this.title = title;
        this.status = status;
        this.errorMessageId = errorMessageId;
        this.detail = errorMessage;

    }

    protected AnswerKingException(
            final String type,
            final String title,
            final HttpStatus status,
            final String errorMessage
    ) {
        this(type, title, status, null, errorMessage);
    }
}
