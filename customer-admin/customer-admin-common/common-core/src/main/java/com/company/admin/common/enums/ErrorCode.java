package com.company.admin.common.enums;

public enum ErrorCode {
    SUCCESS(200, "操作成功"),
    
    SYSTEM_ERROR(500, "系统异常，请稍后重试"),
    PARAM_ERROR(400, "参数错误"),
    
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_DISABLED(1002, "用户已被禁用"),
    USER_PASSWORD_ERROR(1003, "用户名或密码错误"),
    USER_EXISTS(1004, "用户已存在"),
    
    TOKEN_INVALID(1101, "令牌无效"),
    TOKEN_EXPIRED(1102, "令牌已过期"),
    TOKEN_BLACKLIST(1103, "令牌已加入黑名单"),
    
    ROLE_NOT_FOUND(2001, "角色不存在"),
    ROLE_DISABLED(2002, "角色已被禁用"),
    ROLE_EXISTS(2003, "角色已存在"),
    
    MENU_NOT_FOUND(3001, "菜单不存在"),
    MENU_EXISTS(3002, "菜单已存在"),
    
    ORG_NOT_FOUND(4001, "机构不存在"),
    ORG_DISABLED(4002, "机构已被禁用"),
    
    AUTH_FAILED(5001, "认证失败"),
    AUTH_FORBIDDEN(5002, "没有权限访问该资源"),
    AUTH_LEVEL_INSUFFICIENT(5003, "授权级别不足");

    private final int code;
    private final String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
