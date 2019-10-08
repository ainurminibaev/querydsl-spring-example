package com.technaxis.querydsl.security.provider;

import com.technaxis.querydsl.model.User;
import com.technaxis.querydsl.repositories.UserRepository;
import com.technaxis.querydsl.security.AuthenticationImpl;
import com.technaxis.querydsl.security.CurrentUser;
import com.technaxis.querydsl.security.token.ApplicationAuthToken;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserPasswordAuthProvider implements AuthenticationProvider {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!authentication.getClass().isAssignableFrom(ApplicationAuthToken.class))
			throw new IllegalArgumentException();

		String password = (String) authentication.getCredentials();
		String email = authentication.getName();
		if (StringUtils.isAnyBlank(password, email)) {
			// FIXME: 28.10.18 normal exception
			throw new BadCredentialsException("empty-param");
		}

		User user = userRepository.findByEmailIgnoreCase(email)
				// FIXME: 28.10.18 normal exception
				.orElseThrow(() -> new BadCredentialsException("user-not-found"));

		if (!passwordEncoder.matches(password, user.getPasswordHash())) {
			throw new BadCredentialsException("wrong-password");
		}

		final String userAuthority = user.getRole().name();
		List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(userAuthority);

		return new AuthenticationImpl(CurrentUser.getBuilder()
				.id(user.getId())
				.username(user.getId().toString())
				.clientId(null)
				.enabled(true)
				.accountNonExpired(true)
				.accountNonLocked(true)
				.credentialsNonExpired(true)
				.authorities(authorityList)
				.build());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(ApplicationAuthToken.class);
	}
}
