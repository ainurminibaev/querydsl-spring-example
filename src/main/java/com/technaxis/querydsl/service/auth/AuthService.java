package com.technaxis.querydsl.service.auth;

import com.technaxis.querydsl.dto.TokenJson;
import com.technaxis.querydsl.security.CurrentUser;
import com.technaxis.querydsl.security.token.ApplicationAuthToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * 29.10.18
 *
 * @author Dinar Rafikov
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;

	public TokenJson authenticate(String login, String password) {
		Long userId = null;
		String role = null;
		TokenJson tokenJson = new TokenJson();
		AbstractAuthenticationToken authenticationToken;
		authenticationToken = new ApplicationAuthToken(login, password);
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		if (authentication == null || !authentication.isAuthenticated()) {
			log.info("Failed to authenticate user [{}]", login);
			throw new BadCredentialsException("bad-credentials");
		}
		Object userDetailsObject = authentication.getDetails();
		if (userDetailsObject instanceof CurrentUser) {
			final CurrentUser userDetails = (CurrentUser) userDetailsObject;
			userId = userDetails.getId();
			final GrantedAuthority authority = userDetails.getAuthorities().toArray(new GrantedAuthority[1])[0];
			role = authority == null ? null : authority.getAuthority();
		}
		log.info("Successfully signed in user [{}] with email [{}]", userId, login);
		tokenJson.setUserId(userId);
		tokenJson.setAuthToken(tokenService.generateToken(userId));
		tokenJson.setRole(role);
		return tokenJson;
	}


}
