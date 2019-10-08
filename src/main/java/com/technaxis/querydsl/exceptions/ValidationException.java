package com.technaxis.querydsl.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * gordeevnm@gmail.com
 * 8/17/18
 */
@Getter
@RequiredArgsConstructor
public class ValidationException extends RuntimeException {
    private final String message;
    private final String field;
}
