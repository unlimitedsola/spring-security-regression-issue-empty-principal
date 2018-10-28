package com.example

import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf()
            .disable()
            .addFilterAt(simpleFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
            .authorizeExchange()
            .anyExchange().authenticated()
            .and()
            .build()
    }

    fun simpleFilter() = AuthenticationWebFilter(NoopAuthenticationManager).apply {
        setAuthenticationConverter(SimpleAuthenticationConverter)
    }

    object SimpleAuthenticationConverter : (ServerWebExchange) -> Mono<Authentication> {
        override fun invoke(exchange: ServerWebExchange): Mono<Authentication> {
            val user = User("test", "N/A", emptyList())
            return PreAuthenticatedAuthenticationToken(user, user.password, user.authorities).toMono()
        }
    }

    object NoopAuthenticationManager : ReactiveAuthenticationManager {

        override fun authenticate(authentication: Authentication): Mono<Authentication> {
            return if (authentication is PreAuthenticatedAuthenticationToken)
                Mono.just(authentication)
            else
                Mono.error(IllegalStateException("Only X.509 Auth accepted."))
        }

    }

}
