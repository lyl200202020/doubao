package com.company.admin.platform.sys.api.controller;

import com.company.admin.common.constant.SecurityConstants;
import com.company.admin.common.result.Result;
import com.company.admin.platform.sys.api.dto.CreateOrgRequest;
import com.company.admin.platform.sys.api.dto.UpdateOrgRequest;
import com.company.admin.platform.sys.api.vo.OrgVO;
import com.company.admin.platform.sys.application.service.OrgApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Organization Management")
@RestController
@RequestMapping("/api/sys/org")
@RequiredArgsConstructor
@Slf4j
public class OrgController {

    private final OrgApplicationService orgApplicationService;

    @Operation(summary = "Get Organization Tree")
    @GetMapping("/tree")
    public Result<List<OrgVO>> getOrgTree() {
        List<OrgVO> orgTree = orgApplicationService.getOrgTree();
        return Result.success(orgTree);
    }

    @Operation(summary = "Get Organization by ID")
    @GetMapping("/{id}")
    public Result<OrgVO> getOrgById(@PathVariable String id) {
        OrgVO org = orgApplicationService.getOrgById(id);
        return Result.success(org);
    }

    @Operation(summary = "Create Organization")
    @PostMapping
    public Result<Void> createOrg(
            @Valid @RequestBody CreateOrgRequest request,
            HttpServletRequest httpRequest) {
        String currentUser = (String) httpRequest.getAttribute(SecurityConstants.CURRENT_USER_ID);
        orgApplicationService.createOrg(request, currentUser);
        return Result.success();
    }

    @Operation(summary = "Update Organization")
    @PutMapping("/{id}")
    public Result<Void> updateOrg(
            @PathVariable String id,
            @Valid @RequestBody UpdateOrgRequest request,
            HttpServletRequest httpRequest) {
        String currentUser = (String) httpRequest.getAttribute(SecurityConstants.CURRENT_USER_ID);
        orgApplicationService.updateOrg(id, request, currentUser);
        return Result.success();
    }

    @Operation(summary = "Delete Organization")
    @DeleteMapping("/{id}")
    public Result<Void> deleteOrg(@PathVariable String id) {
        orgApplicationService.deleteOrg(id);
        return Result.success();
    }

    @Operation(summary = "Update Organization Status")
    @PatchMapping("/{id}/status")
    public Result<Void> updateOrgStatus(
            @PathVariable String id,
            @RequestParam Integer status,
            HttpServletRequest httpRequest) {
        String currentUser = (String) httpRequest.getAttribute(SecurityConstants.CURRENT_USER_ID);
        orgApplicationService.updateOrgStatus(id, status, currentUser);
        return Result.success();
    }
}
