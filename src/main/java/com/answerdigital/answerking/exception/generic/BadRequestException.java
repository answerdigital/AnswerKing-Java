package com.answerdigital.answerking.exception.generic;

import com.answerdigital.answerking.exception.AnswerKingException;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BadRequestException extends AnswerKingException {

    private static final String TYPE = "https://www.rfc-editor.org/rfc/rfc7231";

    private static final String TITLE = "Bad Request Exception";

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String DETAIL = null;

    public BadRequestException(final String error) {
        super(TYPE, TITLE, STATUS, DETAIL, error);
    }
    public BadRequestException(final Map<String, Collection<String>> errors, String detail) {
        super(TYPE, TITLE, STATUS, detail, errors);
    }
}
