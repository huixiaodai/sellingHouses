package com.sellinghouses.salescontrol.common.util;

import com.sellinghouses.salescontrol.common.config.JwtProperties;
import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final String CLAIM_USERNAME = "username";

    private static final String CLAIM_ROLE_CODE = "roleCode";

    private static final String CLAIM_TOKEN_VERSION = "tokenVersion";

    private final JwtProperties jwtProperties;

    public String generateToken(Long userId, String username, String roleCode, Integer tokenVersion) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtProperties.getExpirationMinutes() * 60);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim(CLAIM_USERNAME, username)
                .claim(CLAIM_ROLE_CODE, roleCode)
                .claim(CLAIM_TOKEN_VERSION, tokenVersion)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey())
                .compact();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }

    public Integer getTokenVersion(Claims claims) {
        Object tokenVersion = claims.get(CLAIM_TOKEN_VERSION);
        if (tokenVersion instanceof Integer value) {
            return value;
        }
        if (tokenVersion instanceof Number value) {
            return value.intValue();
        }
        throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
