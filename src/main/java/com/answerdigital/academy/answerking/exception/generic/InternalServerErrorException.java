package com.answerdigital.academy.answerking.exception.generic;

import com.answerdigital.academy.answerking.exception.AnswerKingException;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;

public class InternalServerErrorException extends AnswerKingException {

    private static final String TYPE = "https://www.rfc-editor.org/rfc/rfc7231";

    private static final String TITLE = "Internal Server Error";

    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public InternalServerErrorException(final Collection<String> detail) {
        super(TYPE, TITLE, STATUS, detail);
    }

    public InternalServerErrorException(final String detail) {
        this(List.of(detail));
    }
}
