package com.answerdigital.answerking.exception.util;

import com.answerdigital.answerking.exception.AnswerKingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Getter
public class ErrorResponse extends ProblemDetail {
    private final String traceId;

    private final String errorMessageId;

    @SneakyThrows(URISyntaxException.class)
    public ErrorResponse(final AnswerKingException exception, final HttpServletRequest request) {
        setType(new URI(exception.getType()));
        setTitle(exception.getTitle());
        setStatus(exception.getStatus());
        setDetail(exception.getDetail());
        setInstance(new URI(request.getRequestURI()));
        this.traceId = CorrelationId.getId();
        this.errorMessageId = exception.getErrorMessageId();

        log.error(String.format(
                "RETURNING EXCEPTION - TYPE: %s - TITLE: %s - STATUS: %s - DETAIL: %s - INSTANCE: %s - TRACEID: %s " +
                        "- ERRORMESSAGEID: %s",
                getType(), getTitle(), getStatus(), getDetail(), getInstance(), traceId, errorMessageId)
        );
    }
}
