package com.technaxis.querydsl.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 29.10.18
 *
 * @author Dinar Rafikov
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public class NotFoundException extends RuntimeException {
	private final String message;
}
