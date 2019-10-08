package com.technaxis.querydsl.service.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 28.10.18
 *
 * @author Dinar Rafikov
 * @version 1.0
 */
@Slf4j
@Service
public class TokenService {
	public static final String USER_ID = "user_id";
	private static final String JWT_TOKEN_KEY = "lol-kek";

	public String generateToken(Long userId) {
		Map<String, Object> tokenData = new HashMap<>();
		tokenData.put(USER_ID, userId);
		tokenData.put("clientType", "user");
		tokenData.put("token_create_date", new Date().getTime());
		Date expirationDate = Date.from(
				LocalDateTime.now()
						.plusMonths(1)
						.atZone(ZoneId.systemDefault())
						.toInstant());
		tokenData.put("token_expiration_date", expirationDate);
		return Jwts.builder()
				.setExpiration(expirationDate)
				.setClaims(tokenData)
				.signWith(SignatureAlgorithm.HS512, JWT_TOKEN_KEY)
				.compact();
	}

	public Optional<Long> getUserId(String authToken) {
		if (StringUtils.isBlank(authToken)) return Optional.empty();
		try {
			val claims = (DefaultClaims) Jwts.parser().setSigningKey(JWT_TOKEN_KEY).parse(authToken).getBody();
			if (claims.get(USER_ID) instanceof Integer) {
				return Optional.of(((Integer) claims.get(USER_ID)).longValue());
			}
			return Optional.of((Long) claims.get(USER_ID));
		} catch (Exception e) {
			log.warn("Invalid token", e);
			return Optional.empty();
		}
	}
}
