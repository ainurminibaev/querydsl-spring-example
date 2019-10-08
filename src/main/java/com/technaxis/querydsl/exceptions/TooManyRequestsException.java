package com.technaxis.querydsl.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Dmitry Sadchikov
 */
@Getter
@AllArgsConstructor
public class TooManyRequestsException extends RuntimeException {
    private Long retryAfter;
}
