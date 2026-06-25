package com.linklingua.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT helper for issuing and parsing signed tokens (HS256).
 *
 * @author LinkLingua
 */
@Component
public class JwtUtil {

    /** Signing secret; must be at least 32 bytes for HS256. */
    @Value("${linklingua.jwt.secret}")
    private String secret;

    /** Token time-to-live in milliseconds. */
    @Value("${linklingua.jwt.ttl}")
    private long ttl;

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Creates a signed JWT carrying the given claims.
     *
     * @param claims custom claims to embed (e.g. userId, username)
     * @return the compact JWT string
     */
    public String createToken(Map<String, Object> claims) {
        Date now = new Date();
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + ttl))
                .signWith(signingKey())
                .compact();
    }

    /**
     * Parses and verifies a JWT, returning its claims.
     *
     * @param token the compact JWT string
     * @return the token claims
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
