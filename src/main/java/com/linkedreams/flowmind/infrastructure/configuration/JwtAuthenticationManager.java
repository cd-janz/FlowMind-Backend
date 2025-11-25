package com.linkedreams.flowmind.infrastructure.configuration;

import com.linkedreams.flowmind.infrastructure.models.BearerToken;
import com.linkedreams.flowmind.infrastructure.redis.User;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtSupport jwt;
    private final ReactiveRedisOperations<String, User> userOps;
    private final ReactiveRedisOperations<String, String> indexOps;
    public JwtAuthenticationManager(
            JwtSupport jwt,
            ReactiveRedisOperations<String, User> userOps,
            ReactiveRedisOperations<String, String> indexOps
    ) {
        this.jwt = jwt;
        this.userOps = userOps;
        this.indexOps = indexOps;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .filter(auth -> auth instanceof BearerToken)
                .cast(BearerToken.class)
                .flatMap(this::validateToken)
                .onErrorMap(error -> {
                    if (error instanceof ResponseStatusException) {
                        return error;
                    } else if (error instanceof IllegalArgumentException) {
                        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, error.getMessage());
                    } else {
                        return new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Authentication error: " + error.getMessage()
                        );
                    }
                });
    }
    private Mono<Authentication> validateToken(BearerToken token){
        if (jwt.isTokenExpired(token)) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Token expired or invalid signature."
            ));
        }
        return jwt.getEmail(token)
                .flatMap(e -> indexOps.opsForValue().get("email:"+e))
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"))
                )
                .flatMap(i -> userOps.opsForValue().get("user:"+i))
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Data consistency is broken, please contact support."))
                )
                .map(u -> {
                    List<SimpleGrantedAuthority> authorities = mapRoleToAuthorities(Integer.parseInt(u.roleValue()));
                    return new UsernamePasswordAuthenticationToken(
                            u.email(),
                            null,
                            authorities
                    );
                });
    }

    private List<SimpleGrantedAuthority> mapRoleToAuthorities(Integer roleValue){
        return switch (roleValue) {
            case 7 -> // 7 (ADMIN)
                    List.of(
                            new SimpleGrantedAuthority("ROLE_ADMIN"),
                            new SimpleGrantedAuthority("ROLE_MODERATOR"),
                            new SimpleGrantedAuthority("ROLE_CLIENT")
                    );
            case 1 -> // 1 (CLIENT)
                    List.of(
                            new SimpleGrantedAuthority("ROLE_CLIENT")
                    );
            default -> Collections.emptyList();
        };
    }
}
