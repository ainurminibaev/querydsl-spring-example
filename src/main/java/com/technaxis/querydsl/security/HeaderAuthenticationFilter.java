package com.technaxis.querydsl.security;

import com.technaxis.querydsl.model.User;
import com.technaxis.querydsl.repositories.UserRepository;
import com.technaxis.querydsl.service.auth.TokenService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Setter
@Slf4j
public class HeaderAuthenticationFilter extends FilterSecurityInterceptor {
	private TokenService tokenService;
	private UserRepository userRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		SecurityContext securityContext = loadSecurityContext(request);
		SecurityContextHolder.setContext(securityContext);

		chain.doFilter(request, response);
	}

	private SecurityContext loadSecurityContext(HttpServletRequest request) {
		String authToken = request.getHeader("Authorization");
		String clientId = request.getHeader("X-ClientId");
		Optional<Long> optionalUserId = tokenService.getUserId(authToken);
		UserDetails userDetails;
		if (optionalUserId.isPresent()) {
			Optional<User> optionalUser = userRepository.findById(optionalUserId.get());
			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(user.getRole().name());
				userDetails = CurrentUser.getBuilder()
						.id(user.getId())
						.username(user.getId().toString())
						.clientId(clientId)
						.enabled(true)
						.accountNonExpired(true)
						.accountNonLocked(true)
						.credentialsNonExpired(true)
						.authorities(authorityList)
						.build();
				return new CustomSecurityContext(userDetails);
			} else {
				log.info("User with id {} not found", optionalUserId);
			}
		}
		userDetails = CurrentUser.getBuilder()
				.username(CurrentUser.ANONYMOUS_AUTHORITY)
				.clientId(clientId)
				.enabled(true)
				.accountNonExpired(true)
				.accountNonLocked(true)
				.credentialsNonExpired(true)
				.authorities(CurrentUser.ANONYMOUS_AUTHORITIES)
				.build();
		return new CustomSecurityContext(userDetails);
	}
}
