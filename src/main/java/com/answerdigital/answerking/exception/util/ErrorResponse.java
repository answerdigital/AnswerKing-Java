package com.answerdigital.answerking.exception.util;

import com.answerdigital.answerking.exception.AnswerKingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
public class ErrorResponse {

    private final String type;

    private final String title;

    private final int status;

    private final String detail;

    private final String instance;

    private final Map<String, Collection<String>> errors = new HashMap<>();

    private final String traceId;

    public ErrorResponse(final AnswerKingException exception, final HttpServletRequest request, boolean validtionProblem) {
        this.type = exception.getType();
        this.title = exception.getTitle();
        this.status = exception.getStatus().value();
        this.detail = exception.getDetail();
        this.instance = request.getRequestURI();
        this.errors.putAll(exception.getErrors());
        this.traceId = CorrelationId.getId();

        log.error(String.format(
                "RETURNING EXCEPTION - TYPE: %s - TITLE: %s - STATUS: %s - DETAIL: %s - INSTANCE: %s - ERRORS: %s - TRACEID: %s",
                type, title, status, detail, instance, errors.toString(), traceId)
        );
    }
    public ErrorResponse(final AnswerKingException exception, final HttpServletRequest request) {
        this.type = exception.getType();
        this.title = exception.getTitle();
        this.status = exception.getStatus().value();
        this.detail = exception.getError();
        this.instance = request.getRequestURI();
        this.traceId = CorrelationId.getId();

        log.error(String.format(
                "RETURNING EXCEPTION - TYPE: %s - TITLE: %s - STATUS: %s - DETAIL: %s - INSTANCE: %s - TRACEID: %s",
                type, title, status, detail, instance, traceId)
        );
    }
}
