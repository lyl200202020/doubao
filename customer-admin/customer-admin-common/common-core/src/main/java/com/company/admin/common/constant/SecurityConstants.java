package com.company.admin.common.constant;

public class SecurityConstants {
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    
    public static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";
    public static final long TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000L;
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;
    
    public static final String CURRENT_USER = "CURRENT_USER";
    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";
    public static final String CURRENT_USERNAME = "CURRENT_USERNAME";
    public static final String CURRENT_REAL_NAME = "CURRENT_REAL_NAME";
    public static final String CURRENT_ORG_ID = "CURRENT_ORG_ID";
    public static final String CURRENT_AUTH_LEVEL = "CURRENT_AUTH_LEVEL";
    
    private SecurityConstants() {
    }
}
