package com.linkedreams.flowmind.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .exceptionHandling(spec -> spec
                        .authenticationEntryPoint(((exchange, ex) ->
                                Mono.fromRunnable(()-> {
                                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                    exchange.getResponse().getHeaders().set(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
                                }))
                        )
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }
}
