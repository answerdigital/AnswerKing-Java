package com.answerdigital.answerking.exception.custom;

import com.answerdigital.answerking.exception.AnswerKingException;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;

public class RetirementException extends AnswerKingException {

    private static final String TYPE = "https://www.rfc-editor.org/rfc/rfc7231";

    private static final String TITLE = "Retirement Exception";

    private static final HttpStatus STATUS = HttpStatus.GONE;

    private static final String DETAIL = null;

    public RetirementException(final Collection<String> errors) {
        super(TYPE, TITLE, STATUS, DETAIL, errors);
    }

    public RetirementException(final String error) {
        this(List.of(error));
    }
}
