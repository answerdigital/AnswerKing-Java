package com.answerdigital.academy.answerking.exception;

import java.util.Collection;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AnswerKingException extends RuntimeException {

    private final String type;

    private final String title;

    private final HttpStatus status;

    private final Collection<String> detail;

    protected AnswerKingException(
            final String type,
            final String title,
            final HttpStatus status,
            final Collection<String> detail
    ) {
        super(detail.toString());
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
    }
}
