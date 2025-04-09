package com.microservices.saliiov.resource.gateway.utils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class TimeUtils {

    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static String getCurrentTimeStamp() {
        return DateTimeFormatter.ofPattern(PATTERN).withZone(ZoneOffset.UTC).format(Instant.now());
    }

    private TimeUtils() {}

}
