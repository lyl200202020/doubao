package com.company.admin.security.config;

import com.company.admin.common.exception.BusinessException;
import com.company.admin.common.enums.ErrorCode;
import com.company.admin.common.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.company.admin.security.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper;
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(this::handleAuthenticationException)
                        .accessDeniedHandler(this::handleAccessDeniedException)
                );
        
        return http.build();
    }
    
    private void handleAuthenticationException(HttpServletRequest request, HttpServletResponse response, 
                                                Exception exception) throws IOException {
        BusinessException businessException;
        if (exception instanceof BusinessException) {
            businessException = (BusinessException) exception;
        } else {
            businessException = new BusinessException(ErrorCode.AUTH_FAILED);
        }
        
        handleException(response, businessException);
    }
    
    private void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response,
                                             Exception exception) throws IOException {
        BusinessException businessException = new BusinessException(ErrorCode.AUTH_FORBIDDEN);
        handleException(response, businessException);
    }
    
    private void handleException(HttpServletResponse response, BusinessException exception) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        Result<?> result = Result.error(exception.getCode(), exception.getMsg());
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
