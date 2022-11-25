package com.answerdigital.answerking.exception.custom;

import com.answerdigital.answerking.exception.AnswerKingException;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;

public class OrderCancelledException extends AnswerKingException {
    private static final String TYPE = "https://www.rfc-editor.org/rfc/rfc7231";

    private static final String TITLE = "Order Cancelled Exception";

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String DETAIL = null;

    public OrderCancelledException(final Collection<String> errors) {
        super(TYPE, TITLE, STATUS, DETAIL, errors);
    }

    public OrderCancelledException(final String error) {
        this(List.of(error));
    }
}
