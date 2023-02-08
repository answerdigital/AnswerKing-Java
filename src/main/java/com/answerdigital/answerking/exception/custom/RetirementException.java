package com.answerdigital.answerking.exception.custom;

import com.answerdigital.answerking.exception.AnswerKingException;
import org.springframework.http.HttpStatus;
/**
 * The class RetirementException represents an exception when an entity is retired.
 */

public class RetirementException extends AnswerKingException {

    private static final String TYPE = "https://www.rfc-editor.org/rfc/rfc7231";

    private static final String TITLE = "Retirement Exception";

    private static final HttpStatus STATUS = HttpStatus.GONE;

    public RetirementException(final String errorMessageId, final String errorMessage) {
        super(TYPE, TITLE, STATUS, errorMessageId, errorMessage);
    }

}
