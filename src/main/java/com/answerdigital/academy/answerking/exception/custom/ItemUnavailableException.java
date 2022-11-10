package com.answerdigital.academy.answerking.exception.custom;

import com.answerdigital.academy.answerking.exception.AnswerKingException;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;

public class ItemUnavailableException extends AnswerKingException {

    private static final String TYPE = "https://www.rfc-editor.org/rfc/rfc7231";

    private static final String TITLE = "Item Unavailable Exception";

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String DETAIL = null;

    public ItemUnavailableException(final Collection<String> errors) {
        super(TYPE, TITLE, STATUS, DETAIL, errors);
    }

    public ItemUnavailableException(final String error) {
        this(List.of(error));
    }
}
