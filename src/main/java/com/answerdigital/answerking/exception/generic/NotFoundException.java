package com.answerdigital.answerking.exception.generic;

import com.answerdigital.answerking.exception.AnswerKingException;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;

public class NotFoundException extends AnswerKingException {

    private static final String TYPE = "https://www.rfc-editor.org/rfc/rfc7231";

    private static final String TITLE = "Not Found Exception";

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String DETAIL = null;

    public NotFoundException(final String errors) {
        super(TYPE, TITLE, STATUS, DETAIL, errors);
    }

}
