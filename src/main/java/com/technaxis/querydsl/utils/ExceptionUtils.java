package com.technaxis.querydsl.utils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Dmitry Sadchikov
 */
public class ExceptionUtils extends org.apache.commons.lang3.exception.ExceptionUtils {

    public static String getShortStackTrace(Throwable throwable) {
        return Stream.of(throwable.getStackTrace())
                .limit(3)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining(" <- "));
    }
}
