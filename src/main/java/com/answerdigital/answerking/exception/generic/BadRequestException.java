package com.answerdigital.answerking.exception.generic;

import com.answerdigital.answerking.exception.AnswerKingException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends AnswerKingException {

    private static final String TYPE = "https://www.rfc-editor.org/rfc/rfc7231";

    private static final String TITLE = "Bad Request Exception";

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public BadRequestException(final String errorMessageId, final String errorMessage) {
        super(TYPE, TITLE, STATUS, errorMessageId, errorMessage);
    }

    public BadRequestException(final String errorMessage) {
        super(TYPE, TITLE, STATUS, errorMessage);
    }
}
