package com.linkedreams.flowmind.infrastructure.configuration;

import com.linkedreams.flowmind.infrastructure.models.BearerToken;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtSupport {
    private final SecretKey key = Keys.hmacShaKeyFor("9rSPQFeqLUhTtn9haBaWzPNMKiF6tVxkJhSd8sH7hyk=".getBytes());
    private final JwtParser parser = Jwts.parser().verifyWith(key).build();

    public BearerToken generate(String email){
        return new BearerToken(Jwts
                .builder()
                .subject(email)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .signWith(key)
                .compact());
    }

    public Mono<String> getEmail(BearerToken token){
        return Mono.just(parser.parseSignedClaims(token.getValue()).getPayload().getSubject());
    }

    public Boolean isTokenExpired(BearerToken token){
        return parser.parseSignedClaims(token.getValue())
                        .getPayload()
                        .getExpiration()
                        .before(Date.from(Instant.now()));
    }

}