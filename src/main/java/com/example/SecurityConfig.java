package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.security.Principal;

/**
 * @author Rob Winch
 */
@Configuration
public class SecurityConfig {

	/**
	 * Overrides the {@link ServerWebExchange#getPrincipal()} from the {@link Context}
	 * @return
	 */
	@Bean
	WebFilter reactorContextWebFilter() {
		return (e,c) ->  {
			Mono<Principal> principal = Mono.subscriberContext()
					.filter(ctx -> ctx.hasKey(Principal.class))
					.flatMap(ctx -> ctx.<Mono<Principal>>get(Principal.class));
			return c.filter(e.mutate().principal(principal).build());
		};
	}

	/**
	 * Authenticates the user as "the-username" by setting the {@link Context}. This is
	 * executed before {@link #reactorContextWebFilter()}
	 * @return
	 */
	@Bean
	@Order(0)
	WebFilter authenticationFilter() {
		Principal p = () -> "the-username";
		return (e,c) ->  c
			.filter(e)
			.subscriberContext(Context.of(Principal.class, Mono.just(p)));
	}
}
