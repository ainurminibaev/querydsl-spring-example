package com.technaxis.querydsl.controller;

import com.technaxis.querydsl.dto.Response;
import com.technaxis.querydsl.dto.TokenJson;
import com.technaxis.querydsl.service.auth.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * 29.10.18
 *
 * @author Dinar Rafikov
 * @version 1.0
 */
@Slf4j
// TODO: 2019-02-15 remove this when authorization becomes relevant
@ApiIgnore
@RestController
@Api(tags = {"Auth"})
@RequestMapping(AuthController.ROOT_URL)
@RequiredArgsConstructor
public class AuthController {
	public static final String ROOT_URL = "/v1";
	public static final String SIGN_IN_URL = "sign-in";

	private final AuthService authService;

	@ApiOperation(value = "Sign in by email and password")
	@ApiResponses({@ApiResponse(code = SC_BAD_REQUEST,
			message = "Account or password not specified: \"empty-param\"; " +
					"account param has invalid email: \"invalid-email\""),
					@ApiResponse(code = SC_UNAUTHORIZED, message = "Authentication failed")})
	@PostMapping(SIGN_IN_URL)
	public ResponseEntity<Response<TokenJson>> signIn(@RequestParam(value = "email") String email,
                                                      @RequestParam(value = "password") String password) {
		email = email.trim();
		password = password.trim();

		log.debug("Signing in with params account [{}]", email);
		if (email.isEmpty() || password.isEmpty()) {
			return new ResponseEntity<>(new Response<>(Response.Error.builder()
					.message("Empty email or password")
					.type(Response.Error.Type.BAD_REQUEST)
					.build()), HttpStatus.BAD_REQUEST);
		}
		try {
			TokenJson authentication = authService.authenticate(email, password);
			return new ResponseEntity<>(new Response<>(authentication), HttpStatus.OK);
		} catch (AuthenticationException e) {
			return new ResponseEntity<>(new Response<>(Response.Error.builder()
					.message("Authentication failed")
					.type(Response.Error.Type.BAD_CREDENTIALS)
					.build()), HttpStatus.UNAUTHORIZED);
		}
	}
}
