package com.company.admin.platform.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "Create Menu Request")
public class CreateMenuRequest {

    @NotBlank(message = "Menu name is required")
    @Schema(description = "Menu Name", example = "User Management", required = true)
    private String menuName;

    @NotNull(message = "Menu type is required")
    @Schema(description = "Menu Type (1-Directory, 2-Menu, 3-Button)", example = "2", required = true)
    private Integer menuType;

    @Schema(description = "Parent Menu ID", example = "1")
    private String parentMenuId;

    @Schema(description = "Menu URL", example = "/system/user")
    private String menuUrl;

    @Schema(description = "Menu Icon", example = "user")
    private String menuIcon;

    @Schema(description = "Permission Identifier", example = "sys:user:view")
    private String perms;

    @Schema(description = "Sort Order", example = "1")
    private Integer sortOrder = 0;

    @Schema(description = "Visible (0-Hidden, 1-Visible)", example = "1")
    private Integer visible = 1;
}
