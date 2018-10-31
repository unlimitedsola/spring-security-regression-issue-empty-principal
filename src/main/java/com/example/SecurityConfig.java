package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.SecurityContextServerWebExchangeWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.WebFilter;


/**
 * @author Rob Winch
 */
@Configuration
public class SecurityConfig {

	@Bean
	SecurityContextServerWebExchangeWebFilter reactorContextWebFilter() {
		return new SecurityContextServerWebExchangeWebFilter();
	}

	@Bean
	@Order(0)
	WebFilter simpleFilter() {
		return (e,c) ->  c
			.filter(e)
			.subscriberContext(ReactiveSecurityContextHolder.withAuthentication(new TestingAuthenticationToken("the-username", "p", "USER")));
	}
}
