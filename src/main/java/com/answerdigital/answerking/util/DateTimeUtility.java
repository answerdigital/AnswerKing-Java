package com.answerdigital.answerking.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public final class DateTimeUtility {

    private DateTimeUtility() {}

    public static String getDateTimeAsString() {
        return ZonedDateTime.now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
