package com.technaxis.querydsl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 29.10.18
 *
 * @author Dinar Rafikov
 * @version 1.0
 */
@Configuration
public class WebConfig {
	@Bean
	public BCryptPasswordEncoder BCCryptEncoder() {
		return new BCryptPasswordEncoder();
	}
}
