package com.company.admin.platform.sys.api.controller;

import com.company.admin.common.constant.SecurityConstants;
import com.company.admin.common.result.PageResult;
import com.company.admin.common.result.Result;
import com.company.admin.platform.sys.api.dto.AssignMenusRequest;
import com.company.admin.platform.sys.api.dto.CreateRoleRequest;
import com.company.admin.platform.sys.api.dto.UpdateRoleRequest;
import com.company.admin.platform.sys.api.vo.RoleVO;
import com.company.admin.platform.sys.application.service.RoleApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Role Management")
@RestController
@RequestMapping("/api/sys/role")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleApplicationService roleApplicationService;

    @Operation(summary = "Page Query Roles")
    @GetMapping("/list")
    public Result<PageResult<RoleVO>> pageRoles(
            @RequestParam(required = false) Integer roleStatus,
            @RequestParam(required = false) String roleName,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<RoleVO> page = roleApplicationService.pageRoles(roleStatus, roleName, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "Get Role Options")
    @GetMapping("/options")
    public Result<List<RoleVO>> getRoleOptions() {
        List<RoleVO> roles = roleApplicationService.getAllRoles();
        return Result.success(roles);
    }

    @Operation(summary = "Get Role by ID")
    @GetMapping("/{id}")
    public Result<RoleVO> getRoleById(@PathVariable String id) {
        RoleVO role = roleApplicationService.getRoleById(id);
        return Result.success(role);
    }

    @Operation(summary = "Create Role")
    @PostMapping
    public Result<Void> createRole(
            @Valid @RequestBody CreateRoleRequest request,
            HttpServletRequest httpRequest) {
        String currentUser = (String) httpRequest.getAttribute(SecurityConstants.CURRENT_USER_ID);
        roleApplicationService.createRole(request, currentUser);
        return Result.success();
    }

    @Operation(summary = "Update Role")
    @PutMapping("/{id}")
    public Result<Void> updateRole(
            @PathVariable String id,
            @Valid @RequestBody UpdateRoleRequest request,
            HttpServletRequest httpRequest) {
        String currentUser = (String) httpRequest.getAttribute(SecurityConstants.CURRENT_USER_ID);
        roleApplicationService.updateRole(id, request, currentUser);
        return Result.success();
    }

    @Operation(summary = "Delete Role")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable String id) {
        roleApplicationService.deleteRole(id);
        return Result.success();
    }

    @Operation(summary = "Toggle Role Status")
    @PatchMapping("/{id}/status")
    public Result<Void> toggleRoleStatus(@PathVariable String id) {
        roleApplicationService.toggleRoleStatus(id);
        return Result.success();
    }

    @Operation(summary = "Assign Menus to Role")
    @PostMapping("/{id}/menus")
    public Result<Void> assignMenus(
            @PathVariable String id,
            @RequestBody AssignMenusRequest request) {
        roleApplicationService.assignMenus(id, request.getMenuIds());
        return Result.success();
    }

    @Operation(summary = "Get Role Menus")
    @GetMapping("/{id}/menus")
    public Result<List<String>> getRoleMenus(@PathVariable String id) {
        List<String> menuIds = roleApplicationService.getRoleMenus(id);
        return Result.success(menuIds);
    }
}
