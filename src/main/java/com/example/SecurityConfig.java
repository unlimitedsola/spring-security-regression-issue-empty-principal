package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.security.Principal;

/**
 * @author Rob Winch
 */
@Configuration
public class SecurityConfig {

	@Bean
	WebFilter reactorContextWebFilter() {
		return (e,c) ->  {
			Mono<Principal> principal = Mono.subscriberContext()
					.filter(ctx -> ctx.hasKey(Principal.class))
					.flatMap(ctx -> ctx.<Mono<Principal>>get(Principal.class));
			return c.filter(new SecurityContextServerWebExchange(e, principal));
		};
	}

	@Bean
	@Order(0)
	WebFilter simpleFilter() {
		Principal p = () -> "the-username";
		return (e,c) ->  c
			.filter(e)
			.subscriberContext(Context.of(Principal.class, Mono.just(p)));
	}

	static class SecurityContextServerWebExchange extends
			ServerWebExchangeDecorator {
		private final Mono<Principal> context;

		public SecurityContextServerWebExchange(ServerWebExchange delegate, Mono<Principal> context) {
			super(delegate);
			this.context = context;
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T extends Principal> Mono<T> getPrincipal() {
			return (Mono<T>) this.context;
		}
	}
}
