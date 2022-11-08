package com.answerdigital.academy.answerking.exception.generic;

import com.answerdigital.academy.answerking.exception.AnswerKingException;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;

public class BadRequestException extends AnswerKingException {

    private static final String TYPE = "https://www.rfc-editor.org/rfc/rfc7231";

    private static final String TITLE = "Bad Request Exception";

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public BadRequestException(final Collection<String> detail) {
        super(TYPE, TITLE, STATUS, detail);
    }

    public BadRequestException(final String detail) {
        this(List.of(detail));
    }
}
