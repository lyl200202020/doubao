package com.company.admin.platform.sys.api.controller;

import com.company.admin.common.constant.SecurityConstants;
import com.company.admin.common.result.Result;
import com.company.admin.platform.sys.application.command.LoginCommand;
import com.company.admin.platform.sys.application.service.AuthApplicationService;
import com.company.admin.platform.sys.application.service.UserApplicationService;
import com.company.admin.platform.sys.api.vo.CurrentUserVO;
import com.company.admin.platform.sys.api.vo.LoginVO;
import com.company.admin.security.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth Management")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthApplicationService authApplicationService;
    private final UserApplicationService userApplicationService;

    @Operation(summary = "User Login")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginCommand command) {
        LoginVO loginVO = authApplicationService.login(command);
        return Result.success(loginVO);
    }

    @Operation(summary = "User Logout")
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        String userId = (String) request.getAttribute(SecurityConstants.CURRENT_USER_ID);
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (token != null && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            token = token.substring(SecurityConstants.TOKEN_PREFIX.length());
            if (userId != null) {
                authApplicationService.logout(userId, token);
            }
            log.info("User logout, userId={}", userId);
        }
        return Result.success();
    }

    @Operation(summary = "Refresh Token")
    @PostMapping("/refresh")
    public Result<LoginVO> refresh(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (token != null && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            token = token.substring(SecurityConstants.TOKEN_PREFIX.length());
            LoginVO loginVO = authApplicationService.refresh(token);
            return Result.success(loginVO);
        }
        return Result.error(401, "Token invalid");
    }

    @Operation(summary = "Get Current User Info")
    @GetMapping("/current")
    public Result<CurrentUserVO> getCurrentUser(HttpServletRequest request) {
        String userId = (String) request.getAttribute(SecurityConstants.CURRENT_USER_ID);
        if (userId == null) {
            return Result.error(401, "Unauthorized");
        }
        CurrentUserVO user = userApplicationService.getCurrentUser(userId);
        if (user == null) {
            return Result.error(404, "User not found");
        }
        return Result.success(user);
    }
}
