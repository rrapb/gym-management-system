package com.ubt.gymms.configurations.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ubt.gymms.services.util.JwtUtil;
import feign.RequestInterceptor;

@Configuration
public class FeignConfig {

	@Autowired
	private JwtUtil jwtUtil;

	@Bean
	public RequestInterceptor requestInterceptor() {
		return requestTemplate -> {
			requestTemplate.header("Authorization", jwtUtil.generateTokenWithBearerPrefix());
		};
	}
}
