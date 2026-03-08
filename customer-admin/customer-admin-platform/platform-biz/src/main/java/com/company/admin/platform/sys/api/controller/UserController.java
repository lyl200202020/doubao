package com.company.admin.platform.sys.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.admin.common.constant.SecurityConstants;
import com.company.admin.common.result.Result;
import com.company.admin.platform.sys.api.dto.CreateUserRequest;
import com.company.admin.platform.sys.api.dto.UpdateUserRequest;
import com.company.admin.platform.sys.api.vo.UserVO;
import com.company.admin.platform.sys.application.service.UserApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Management")
@RestController
@RequestMapping("/api/sys/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserApplicationService userApplicationService;

    @Operation(summary = "Page Query Users")
    @GetMapping("/list")
    public Result<Page<UserVO>> pageUsers(
            @RequestParam(name = "orgId", required = false) String orgId,
            @RequestParam(name = "roleId", required = false) String roleId,
            @RequestParam(name = "userStatus", required = false) Integer userStatus,
            @RequestParam(name = "userName", required = false) String userName,
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<UserVO> page = userApplicationService.pageUsers(orgId, roleId, userStatus, userName, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "Get User by ID")
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable String id) {
        UserVO user = userApplicationService.getUserById(id);
        return Result.success(user);
    }

    @Operation(summary = "Create User")
    @PostMapping
    public Result<Void> createUser(
            @Valid @RequestBody CreateUserRequest request,
            HttpServletRequest httpRequest) {
        String currentUser = (String) httpRequest.getAttribute(SecurityConstants.CURRENT_USER_ID);
        userApplicationService.createUser(request, currentUser);
        return Result.success();
    }

    @Operation(summary = "Update User")
    @PutMapping("/{id}")
    public Result<Void> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRequest request,
            HttpServletRequest httpRequest) {
        String currentUser = (String) httpRequest.getAttribute(SecurityConstants.CURRENT_USER_ID);
        userApplicationService.updateUser(id, request, currentUser);
        return Result.success();
    }

    @Operation(summary = "Delete User")
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable String id) {
        userApplicationService.deleteUser(id);
        return Result.success();
    }

    @Operation(summary = "Reset Password")
    @PostMapping("/{id}/reset-pwd")
    public Result<Void> resetPassword(@PathVariable String id) {
        userApplicationService.resetPassword(id);
        return Result.success();
    }

    @Operation(summary = "Update User Status")
    @PatchMapping("/{id}/status")
    public Result<Void> updateUserStatus(
            @PathVariable String id,
            @RequestParam Integer status) {
        userApplicationService.updateUserStatus(id, status);
        return Result.success();
    }
}
