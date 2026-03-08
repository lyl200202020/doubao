package com.company.admin.platform.sys.api.controller;

import com.company.admin.common.constant.SecurityConstants;
import com.company.admin.common.result.Result;
import com.company.admin.platform.sys.api.dto.AssignMenusRequest;
import com.company.admin.platform.sys.api.dto.CreateMenuRequest;
import com.company.admin.platform.sys.api.dto.UpdateMenuRequest;
import com.company.admin.platform.sys.api.vo.MenuVO;
import com.company.admin.platform.sys.application.service.MenuApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Menu Management")
@RestController
@RequestMapping("/api/sys/menu")
@RequiredArgsConstructor
@Slf4j
public class MenuController {

    private final MenuApplicationService menuApplicationService;

    @Operation(summary = "Get Menu Tree")
    @GetMapping("/tree")
    public Result<List<MenuVO>> getMenuTree() {
        List<MenuVO> menuTree = menuApplicationService.getMenuTree();
        return Result.success(menuTree);
    }

    @Operation(summary = "Get Menu by ID")
    @GetMapping("/{id}")
    public Result<MenuVO> getMenuById(@PathVariable String id) {
        MenuVO menu = menuApplicationService.getMenuById(id);
        return Result.success(menu);
    }

    @Operation(summary = "Create Menu")
    @PostMapping
    public Result<Void> createMenu(
            @Valid @RequestBody CreateMenuRequest request,
            HttpServletRequest httpRequest) {
        String currentUser = (String) httpRequest.getAttribute(SecurityConstants.CURRENT_USER_ID);
        menuApplicationService.createMenu(request, currentUser);
        return Result.success();
    }

    @Operation(summary = "Update Menu")
    @PutMapping("/{id}")
    public Result<Void> updateMenu(
            @PathVariable String id,
            @Valid @RequestBody UpdateMenuRequest request,
            HttpServletRequest httpRequest) {
        String currentUser = (String) httpRequest.getAttribute(SecurityConstants.CURRENT_USER_ID);
        menuApplicationService.updateMenu(id, request, currentUser);
        return Result.success();
    }

    @Operation(summary = "Delete Menu")
    @DeleteMapping("/{id}")
    public Result<Void> deleteMenu(@PathVariable String id) {
        menuApplicationService.deleteMenu(id);
        return Result.success();
    }

    @Operation(summary = "Assign Menus to Role")
    @PostMapping("/{roleId}/menus")
    public Result<Void> assignMenusToRole(
            @PathVariable String roleId,
            @Valid @RequestBody AssignMenusRequest request) {
        menuApplicationService.assignMenusToRole(roleId, request);
        return Result.success();
    }
}
