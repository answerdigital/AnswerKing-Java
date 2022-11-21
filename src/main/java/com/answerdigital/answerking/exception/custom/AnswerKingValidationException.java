package com.answerdigital.answerking.exception.custom;

import com.answerdigital.answerking.exception.AnswerKingException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.Map;

@Getter
public class AnswerKingValidationException extends AnswerKingException {

    private static final String TYPE = "https://www.rfc-editor.org/rfc/rfc7231";

    private static final String TITLE = "Validation Exception";

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String DETAIL = "One or more validation errors occurred";

    private final Map<String, Collection<String>> errors;

    public AnswerKingValidationException(final Map<String, Collection<String>> errors) {
        super(TYPE, TITLE, STATUS, DETAIL);
        this.errors = errors;
    }
}
