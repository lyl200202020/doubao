package com.company.admin.platform.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Data
@Schema(description = "Update Role Request")
public class UpdateRoleRequest {

    @NotBlank(message = "Role code is required")
    @Schema(description = "Role Code", example = "manager", required = true)
    private String roleCode;

    @NotBlank(message = "Role name is required")
    @Schema(description = "Role Name", example = "Manager", required = true)
    private String roleName;

    @Schema(description = "Role Description", example = "System Manager")
    private String roleDesc;
}
