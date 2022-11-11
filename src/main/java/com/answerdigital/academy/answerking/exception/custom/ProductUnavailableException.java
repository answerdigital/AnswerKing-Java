package com.answerdigital.academy.answerking.exception.custom;

import com.answerdigital.academy.answerking.exception.AnswerKingException;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;

public class ProductUnavailableException extends AnswerKingException {

    private static final String TYPE = "https://www.rfc-editor.org/rfc/rfc7231";

    private static final String TITLE = "Product Unavailable Exception";

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String DETAIL = null;

    public ProductUnavailableException(final Collection<String> errors) {
        super(TYPE, TITLE, STATUS, DETAIL, errors);
    }

    public ProductUnavailableException(final String error) {
        this(List.of(error));
    }
}
