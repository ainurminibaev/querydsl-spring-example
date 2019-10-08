package com.technaxis.querydsl.config;

import com.technaxis.querydsl.controller.FileController;
import com.technaxis.querydsl.controller.UserController;
import com.technaxis.querydsl.repositories.UserRepository;
import com.technaxis.querydsl.security.HeaderAuthenticationFilter;
import com.technaxis.querydsl.security.provider.UserPasswordAuthProvider;
import com.technaxis.querydsl.service.auth.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserRepository userRepository;
	private final TokenService tokenService;
	private final UserPasswordAuthProvider userPasswordAuthProvider;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		HeaderAuthenticationFilter filter = new HeaderAuthenticationFilter();
		filter.setTokenService(tokenService);
		filter.setUserRepository(userRepository);
		http
				.csrf().disable()
				.requestCache().disable()
				.rememberMe().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilter(filter)
				.authorizeRequests()
				.antMatchers("/v1/user/sign-up/**",
						"/v1/user/sign-in").permitAll()
				.antMatchers(FileController.ROOT_URL + "/**").permitAll()
				.antMatchers(UserController.ROOT_URL + "/**").permitAll()
				.antMatchers("/v1/**").authenticated()
				.and()
				.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler())
				.and()
				.logout().permitAll();
	}

	@Autowired
	public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(userPasswordAuthProvider);
	}

	public static class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
		@Override
		public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
		}
	}
}
