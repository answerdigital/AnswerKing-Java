package com.answerdigital.answerking.util;

import org.junit.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateTimeUtilityTest {

    @Test
    public void testGetDateTimeAsString() {
        final String correctFormat = ZonedDateTime.now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        final String utilityFormat = DateTimeUtility.getDateTimeAsString();
        assertEquals(correctFormat, utilityFormat);
    }
}

