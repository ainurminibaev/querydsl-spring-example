package com.technaxis.querydsl.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * 28.10.2018
 *
 * @author Dinar Rafikov
 * @version 1.0
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
	private T result;
	private Error error;

	public Response(T result) {
		setResult(result);
	}

	public Response(Error error) {
		setError(error);
	}

	@Setter
	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Error {
		private String message;
		private Type type;
		@Singular
		private List<Detail> details;

		@Getter
		@AllArgsConstructor
		public enum Type {
			USER_NOT_FOUND("user-not-found"),
			USER_NOT_REGISTERED("user-not-registered"),
			NOT_FOUND("not-found"),
			NO_MATCH("no-match"),
			ALREADY_EXIST("already-exist"),
			FORBIDDEN("forbidden"),
			NOT_AUTHORIZED("not-authorized"),
			BAD_REQUEST("bad-request"),
			BAD_GATEWAY("bad-gateway"),
			GATEWAY_TIMEOUT("gateway-timeout"),
			REQUEST_FAILED("request-failed"),
			BAD_CREDENTIALS("bad-credentials"),
			TOO_MANY_REQUESTS("too-many-requests"),
			VALIDATION_ERROR("validation-error"),
			GONE("gone");

			private String type;
		}

		@Setter
		@Getter
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		@JsonInclude(JsonInclude.Include.NON_NULL)
		public static class Detail {
			private String message;
			private Type type;
			private String error;
			private String target;

			@Getter
			@AllArgsConstructor
			public enum Type {
				INVALID_PASSWORD("invalid-password"),
				INVALID_EMAIL("invalid-email"),
				NOT_CORRECT("not-correct"),
				EMPTY_PARAM("empty-param"),
				DUPLICATE("duplicate");
				private String type;
			}
		}
	}
}