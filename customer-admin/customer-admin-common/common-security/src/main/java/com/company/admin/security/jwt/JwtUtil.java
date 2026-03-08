package com.company.admin.security.jwt;

import com.company.admin.common.exception.BusinessException;
import com.company.admin.common.enums.ErrorCode;
import com.company.admin.security.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    
    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_USER_CODE = "userCode";
    public static final String CLAIM_USER_NAME = "userName";
    public static final String CLAIM_ORG_ID = "orgId";
    public static final String CLAIM_AUTH_LEVEL = "authLevel";
    public static final String CLAIM_PERMISSIONS = "permissions";

    private final JwtProperties jwtProperties;
    
    private SecretKey secretKey;
    
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(
                jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String userId, String userCode, String userName, 
                                       String orgId, Integer authLevel) {
        return generateToken(userId, userCode, userName, orgId, authLevel, new HashMap<>());
    }

    public String generateToken(String userId, String userCode, String userName,
                                       String orgId, Integer authLevel, Map<String, Object> extraClaims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getExpiration());
        
        JwtBuilder builder = Jwts.builder()
                .id(userId)
                .subject(userCode)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .claim(CLAIM_USER_ID, userId)
                .claim(CLAIM_USER_CODE, userCode)
                .claim(CLAIM_USER_NAME, userName)
                .claim(CLAIM_ORG_ID, orgId)
                .claim(CLAIM_AUTH_LEVEL, authLevel);
        
        extraClaims.forEach(builder::claim);
        
        return builder.compact();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException | MalformedJwtException | 
                 SignatureException | IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.TOKEN_INVALID);
        }
    }

    public String getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.get(CLAIM_USER_ID, String.class);
    }

    public String getUserCode(String token) {
        Claims claims = parseToken(token);
        return claims.get(CLAIM_USER_CODE, String.class);
    }

    public String getUserName(String token) {
        Claims claims = parseToken(token);
        return claims.get(CLAIM_USER_NAME, String.class);
    }

    public String getOrgId(String token) {
        Claims claims = parseToken(token);
        return claims.get(CLAIM_ORG_ID, String.class);
    }

    public Integer getAuthLevel(String token) {
        Claims claims = parseToken(token);
        return claims.get(CLAIM_AUTH_LEVEL, Integer.class);
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
}
