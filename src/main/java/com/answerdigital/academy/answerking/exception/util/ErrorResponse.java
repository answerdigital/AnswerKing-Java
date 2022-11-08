package com.answerdigital.academy.answerking.exception.util;

import com.answerdigital.academy.answerking.exception.AnswerKingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ErrorResponse {

    private final String type;

    private final String title;

    private final int status;

    private final String detail;

    private final String instance;

    public ErrorResponse(final AnswerKingException e) {
        this.type = e.getType();
        this.title = e.getTitle();
        this.status = e.getStatus().value();
        this.detail = e.getDetail().toString();
        this.instance = CorrelationId.getId();

        log.error(String.format(
                "RETURNING EXCEPTION - TYPE: %s - TITLE: %s - STATUS: %s - DETAIL: %s - INSTANCE: %s",
                type, title, status, detail, instance)
        );
    }
}
