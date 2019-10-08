package com.technaxis.querydsl.exceptions;

import lombok.NoArgsConstructor;

/**
 * gordeevnm@gmail.com
 * 6/27/18
 */
@NoArgsConstructor
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
