package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.SecurityContextServerWebExchangeWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import reactor.core.publisher.Mono;


/**
 * @author Rob Winch
 */
@EnableWebFluxSecurity
public class SecurityConfig {
	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		return http
				.securityMatcher(e -> ServerWebExchangeMatcher.MatchResult.notMatch())
				.csrf().disable()
				.addFilterAt(simpleFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
				.authorizeExchange()
					.anyExchange().authenticated()
					.and()
				.build();
	}

	@Bean
	SecurityContextServerWebExchangeWebFilter reactorContextWebFilter() {
		return new SecurityContextServerWebExchangeWebFilter();
	}

	@Bean
	@Order(0)
	AuthenticationWebFilter simpleFilter() {
		AuthenticationWebFilter result = new AuthenticationWebFilter(new NoopAuthenticationManager());
		result.setAuthenticationConverter(e -> Mono.just(new TestingAuthenticationToken("the-username", "p", "ROLE_USER")));
		return result;
	}

	static class NoopAuthenticationManager implements ReactiveAuthenticationManager {
		@Override
		public Mono<Authentication> authenticate(Authentication authentication) {
			return Mono.just(authentication);
		}
	}
}
