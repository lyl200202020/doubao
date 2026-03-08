package com.company.admin.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    
    private String secretKey = "customer-admin-jwt-secret-key-2024-auth-platform";
    
    private Long expiration = 86400000L;
}
