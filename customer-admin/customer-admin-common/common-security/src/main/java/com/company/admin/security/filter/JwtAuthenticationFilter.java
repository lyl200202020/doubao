package com.company.admin.security.filter;

import com.company.admin.common.constant.SecurityConstants;
import com.company.admin.common.exception.BusinessException;
import com.company.admin.common.enums.ErrorCode;
import com.company.admin.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final StringRedisTemplate stringRedisTemplate;
    private final JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        
        if (StringUtils.hasText(token)) {
            try {
                if (jwtUtil.isTokenExpired(token)) {
                    throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
                }
                
                String tokenKey = SecurityConstants.TOKEN_BLACKLIST_PREFIX + token;
                if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(tokenKey))) {
                    throw new BusinessException(ErrorCode.TOKEN_BLACKLIST);
                }
                
                Claims claims = jwtUtil.parseToken(token);
                String userId = claims.get(JwtUtil.CLAIM_USER_ID, String.class);
                String userCode = claims.get(JwtUtil.CLAIM_USER_CODE, String.class);
                String userName = claims.get(JwtUtil.CLAIM_USER_NAME, String.class);
                String orgId = claims.get(JwtUtil.CLAIM_ORG_ID, String.class);
                Integer authLevel = claims.get(JwtUtil.CLAIM_AUTH_LEVEL, Integer.class);
                
                request.setAttribute(SecurityConstants.CURRENT_USER_ID, userId);
                request.setAttribute(SecurityConstants.CURRENT_USERNAME, userCode);
                request.setAttribute(SecurityConstants.CURRENT_REAL_NAME, userName);
                request.setAttribute(SecurityConstants.CURRENT_ORG_ID, orgId);
                request.setAttribute(SecurityConstants.CURRENT_AUTH_LEVEL, authLevel);
                
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
            } catch (BusinessException e) {
                request.setAttribute("jwtException", e);
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(SecurityConstants.JWT_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(SecurityConstants.JWT_PREFIX)) {
            return bearerToken.substring(SecurityConstants.JWT_PREFIX.length());
        }
        return null;
    }
}
