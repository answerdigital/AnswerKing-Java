package com.answerdigital.answerking.exception.util;

import lombok.NoArgsConstructor;
import org.slf4j.MDC;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CorrelationId {

    static final String CORRELATION_ID = "Correlation-Id";

    public static String getId() {
        if (null == MDC.get(CORRELATION_ID)) {
            setId(UUID.randomUUID().toString());
        }
        return MDC.get(CORRELATION_ID);
    }

    public static void setId(final String correlationId) {
        MDC.put(CORRELATION_ID, correlationId);
    }
}

