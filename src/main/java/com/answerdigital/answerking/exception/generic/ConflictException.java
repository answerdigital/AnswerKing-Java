package com.answerdigital.answerking.exception.generic;

import com.answerdigital.answerking.exception.AnswerKingException;
import org.springframework.http.HttpStatus;

public class ConflictException extends AnswerKingException {

    private static final String TYPE = "https://www.rfc-editor.org/rfc/rfc7231";

    private static final String TITLE = "Conflict Exception";

    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    public ConflictException(final String errors) {
        super(TYPE, TITLE, STATUS, errors);
    }

}
