package com.technaxis.querydsl.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class ApplicationAuthToken extends UsernamePasswordAuthenticationToken {
	public ApplicationAuthToken(String email, String password) {
		super(email, password);
	}
}
