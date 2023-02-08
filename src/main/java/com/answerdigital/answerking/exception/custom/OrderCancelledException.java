package com.answerdigital.answerking.exception.custom;

import com.answerdigital.answerking.exception.AnswerKingException;
import org.springframework.http.HttpStatus;

/**
 * The class OrderCancelledException represents an exception when an
 * Order {@link com.answerdigital.answerking.model.Product} has previously been cancelled.
 */
public class OrderCancelledException extends AnswerKingException {
    private static final String TYPE = "https://www.rfc-editor.org/rfc/rfc7231";

    private static final String TITLE = "Order Cancelled Exception";

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public OrderCancelledException(final String errorMessageId, final String errorMessage) {
        super(TYPE, TITLE, STATUS, errorMessageId, errorMessage);
    }
}
