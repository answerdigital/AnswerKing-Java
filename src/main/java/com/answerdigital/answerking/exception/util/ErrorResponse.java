package com.answerdigital.answerking.exception.util;

import com.answerdigital.answerking.exception.AnswerKingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Getter
public class ErrorResponse {

    private final String type;

    private final String title;

    private final int status;

    private final String detail;

    private final String instance;

    private final String traceId;

    public ErrorResponse(final AnswerKingException exception, final HttpServletRequest request) {
        this.type = exception.getType();
        this.title = exception.getTitle();
        this.status = exception.getStatus().value();
        this.detail = exception.getDetail();
        this.instance = request.getRequestURI();
        this.traceId = CorrelationId.getId();

        log.error(String.format(
                "RETURNING EXCEPTION - TYPE: %s - TITLE: %s - STATUS: %s - DETAIL: %s - INSTANCE: %s - TRACEID: %s",
                type, title, status, detail, instance, traceId)
        );
    }
}
