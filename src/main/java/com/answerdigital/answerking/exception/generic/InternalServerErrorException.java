package com.answerdigital.answerking.exception.generic;

import com.answerdigital.answerking.exception.AnswerKingException;
import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends AnswerKingException {

    private static final String TYPE = "https://www.rfc-editor.org/rfc/rfc7231";

    private static final String TITLE = "Internal Server Error";

    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public InternalServerErrorException(final String errors) {
        super(TYPE, TITLE, STATUS, errors);
    }

}
